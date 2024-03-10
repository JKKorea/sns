package com.jk.sns.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jk.sns.controller.request.PostModifyRequest;
import com.jk.sns.controller.request.PostWriteRequest;
import com.jk.sns.exception.ErrorCode;
import com.jk.sns.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void 포스트작성() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostWriteRequest("title", "body"))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트작성시_로그인한상태가_아니라면_에러발생() throws Exception {
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostWriteRequest("title", "body"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    void 포스트수정시_로그인한상태가_아니라면_에러발생() throws Exception {
        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }


    @Test
    @WithMockUser
    void 포스트수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception {
        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트수정시_수정하려는글이_없다면_에러발생() throws Exception {
        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트수정시_데이터베이스_에러_발생시_에러발생() throws Exception {
        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
            .andDo(print())
            .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    void 포스트삭제시_로그인한상태가_아니라면_에러발생() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_TOKEN.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제시_본인이_작성한_글이_아니라면_에러발생() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제시_수정하려는글이_없다면_에러발생() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    void 포스트삭제시_데이터베이스_에러_발생시_에러발생() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is(ErrorCode.DATABASE_ERROR.getStatus().value()));
    }
}
