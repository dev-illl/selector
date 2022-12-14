package com.selector.settings;

import com.selector.WithAccount;
import com.selector.account.AccountRepository;
import com.selector.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class SettingsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @WithAccount("sangyeong")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception{
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("sangyeong")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception{
        String introduce = "짧은 한줄 자기소개 예시입니다.";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("introduce", introduce)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account sangyeong = accountRepository.findByNickName("sangyeong");
        assertEquals(introduce, sangyeong.getIntroduce());

    }

    @WithAccount("sangyeong")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String introduce = "길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 너무나도 길게 소개를 수정하는 경우. ";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                        .param("introduce", introduce)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account sangyeong = accountRepository.findByNickName("sangyeong");
        assertNull(sangyeong.getIntroduce());
    }

    @WithAccount("sangyeong")
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePassword_form() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("sangyeong")
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void updatePassword_success() throws Exception{
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "12345678")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PASSWORD_URL))
                .andExpect(flash().attributeExists("message"));

        Account sangyeong = accountRepository.findByNickName("sangyeong");
        assertTrue(passwordEncoder.matches("12345678", sangyeong.getPassword()));
    }

    @WithAccount("sangyeong")
    @DisplayName("패스워드 수정 - 입력값 에러 - 패스워드 불일치")
    @Test
    void updatePassword_fail() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_PASSWORD_URL)
                        .param("newPassword", "12345678")
                        .param("newPasswordConfirm", "11111111")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }

    @WithAccount("sangyeong")
    @DisplayName("닉네임 수정 폼")
    @Test
    void updateAccountForm() throws Exception{
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nickNameForm"));
    }


    @WithAccount("sangyeong")
    @Test
    @DisplayName("닉네임 수정 TEST - 입력값 정상")
    void updateAccount_success() throws Exception{
        String newNickName = "test";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                .param("nickName", newNickName)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickName("test"));
    }

    @WithAccount("sangyeong")
    @DisplayName("닉네임 수정 TEST - 입력값 에러")
    @Test
    void updateAccount_fail() throws Exception{
        String newNickName = "¯\\_(ツ)_/¯";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                .param("nickName", newNickName)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nickNameForm"));
    }

}