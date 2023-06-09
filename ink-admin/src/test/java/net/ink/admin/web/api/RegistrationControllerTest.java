package net.ink.admin.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ink.admin.service.AdminMemberService;
import net.ink.admin.web.AbstractControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest extends AbstractControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminMemberService adminMemberService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private RegistrationController.RegisterRequest validRegisterRequest;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegistrationController.RegisterRequest();
        validRegisterRequest.setEmail("testUser123@example.com");
        validRegisterRequest.setNickname("testNick");
        validRegisterRequest.setPassword("Test@1234");
    }

    @Test
    @DisplayName("회원가입 성공")
    void register_success() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("ok"));
    }

    @Test
    @DisplayName("회원가입 실패 - 유효성 검사 실패")
    void register_validationFailure() throws Exception {
        RegistrationController.RegisterRequest invalidRequest = new RegistrationController.RegisterRequest();
        invalidRequest.setEmail("test");
        invalidRequest.setNickname("t");
        invalidRequest.setPassword("test123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 유효성 검사 실패")
    void register_validationFailure_emailInvalidFormat() throws Exception {
        RegistrationController.RegisterRequest invalidRequest = new RegistrationController.RegisterRequest();
        invalidRequest.setEmail("testUser123");
        invalidRequest.setNickname("testNick");
        invalidRequest.setPassword("Test@1234");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 유효성 검사 실패 (닉네임 길이가 너무 김)")
    void register_validationFailure_nicknameTooLong() throws Exception {
        RegistrationController.RegisterRequest invalidRequest = new RegistrationController.RegisterRequest();
        invalidRequest.setEmail("testUser123@example.com");
        invalidRequest.setNickname("testNicknameTooLong");
        invalidRequest.setPassword("Test@1234");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호에 특수문자가 없음")
    void register_validationFailure_passwordWithoutSpecialCharacters() throws Exception {
        RegistrationController.RegisterRequest invalidRequest = new RegistrationController.RegisterRequest();
        invalidRequest.setEmail("testUser123@example.com");
        invalidRequest.setNickname("testNick");
        invalidRequest.setPassword("Test12345");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호에 숫자가 없음")
    void register_validationFailure_passwordWithoutDigits() throws Exception {
        RegistrationController.RegisterRequest invalidRequest = new RegistrationController.RegisterRequest();
        invalidRequest.setEmail("testUser123@example.com");
        invalidRequest.setNickname("testNick");
        invalidRequest.setPassword("Test@abcd");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
