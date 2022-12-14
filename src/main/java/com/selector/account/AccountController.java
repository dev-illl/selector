package com.selector.account;

import com.selector.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final AccountService accountService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }
        Account account;
        try {
            account = accountService.processNewAccount(signUpForm);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "account/sign-up";
        }
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String CheckEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if(account == null){
            model.addAttribute("error", "wrong email");
            return view;
        }

        if(!account.isValidToken(token)){
            model.addAttribute("error", "wrong token");
            return view;
        }

        accountService.completeSignUp(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickName", account.getNickName());
        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account, Model model){
        if(!account.canSendConfirmEmail()){
            model.addAttribute("error", "?????? ????????? 1????????? ????????? ?????? ???????????????.");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }
        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/profile/{nickName}")
    public String viewProfile(@PathVariable String nickName, Model model, @CurrentUser Account account){
        Account byNickName = accountRepository.findByNickName(nickName);
        if(nickName == null){
            throw new IllegalArgumentException(nickName + " ??? ???????????? ???????????? ????????????.");
        }
        model.addAttribute(byNickName);
        model.addAttribute("isOwner", byNickName.equals(account));
        return "account/profile";
    }

    @GetMapping("/email-login")
    public String emailLoginForm(){
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String sendEmailLoginLink(String email, Model model, RedirectAttributes redirectAttributes){
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            model.addAttribute("error", "????????? ????????? ????????? ????????????.");
            return "account/email-login";
        }

        if(!account.canSendConfirmEmail()){
            model.addAttribute("error", "????????? ???????????? 1?????? ?????? ????????? ??? ????????????.");
            return "account/email-login";
        }
        accountService.sendLoginLink(account);
        redirectAttributes.addFlashAttribute("message" , " login link ????????? ?????? ???????????????. ????????? ?????? ?????????");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/logged-in-by-email";
        if(account == null || !account.isValidToken(token)){
            model.addAttribute("error", "???????????? ??? ????????????.");
            return view;
        }
        accountService.login(account);
        return view;
    }

}
