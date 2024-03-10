package com.jk.sns.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.sns.controller.request.UserJoinRequest;
import com.jk.sns.controller.request.UserLoginRequest;
import com.jk.sns.exception.ErrorCode;
import com.jk.sns.exception.SimpleSnsApplicationException;
import com.jk.sns.model.User;
import com.jk.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    public void 회원가입() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest("name", "password"))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 회원가입시_같은_아이디로_회원가입하면_에러발생() throws Exception {
        String userName = "name";
        String password = "password";
        when(userService.join(userName, password)).thenThrow(
            new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest("name", "password"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    public void 로그인() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenReturn("testToken");

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 로그인시_비밀번호가_다르면_에러발생() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(
            new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));
    }
}