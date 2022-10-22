package com.selector.account;

import com.selector.domain.Account;
import com.selector.settings.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    public Account processNewAccount(SignUpForm signUpForm) {
        confirmPassword(signUpForm);
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickName(signUpForm.getNickName())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .updatedMyQuestionsByWeb(true)
                .commentedMyQuestionsByWeb(true)
                .build();
        return accountRepository.save(account);
    }

    public void confirmPassword(SignUpForm signUpForm){
        System.out.println("1차 비밀번호 : " + signUpForm.getPassword() + " 2차 비밀번호 : " + signUpForm.getConfirmPassword());
        if(!signUpForm.getPassword().equals(signUpForm.getConfirmPassword())){
            throw new IllegalStateException("1차 비밀번호와 2차 비밀번호가 일치하지 않습니다.");
        }
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("Selector, 회원 가입 인증");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrNickName) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickName);
        if(account == null){
            account = accountRepository.findByNickName(emailOrNickName);
        }

        if(account == null){
            throw new UsernameNotFoundException(emailOrNickName);
        }

        return new UserAccount(account);
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void updateProfile(Account account, Profile profile) {
        account.setIntroduce(profile.getIntroduce());
        account.setInstagramUrl(profile.getInstagramUrl());
        account.setOccupation(profile.getOccupation());
        account.setInterest(profile.getInterest());
        account.setProfileImage(profile.getProfileImage());
        accountRepository.save(account);
    }


    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }
}
