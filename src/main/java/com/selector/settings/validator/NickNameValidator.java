package com.selector.settings.validator;

import com.selector.account.AccountRepository;
import com.selector.domain.Account;
import com.selector.settings.form.NickNameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NickNameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NickNameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NickNameForm nickNameForm = (NickNameForm) target;
        Account byNickName = accountRepository.findByNickName(nickNameForm.getNickName());
        if(byNickName != null){
            errors.rejectValue("nickName", "wrong.value", "입력하신 닉네임은 사용할 수 없습니다.");
        }
    }
}
