package com.jk.sns.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jk.sns.exception.ErrorCode;
import com.jk.sns.exception.SimpleSnsApplicationException;
import com.jk.sns.fixture.UserEntityFixture;
import com.jk.sns.model.entity.PostEntity;
import com.jk.sns.repository.PostEntityRepository;
import com.jk.sns.repository.UserEntityRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    PostEntityRepository postEntityRepository;

    @Test
    void 포스트_생성시_정상동작한다() {
        String userName = "name";
        String password = "password";
        String title = "title";
        String body = "body";

        when(userEntityRepository.findByUserName(userName)).thenReturn(
            Optional.of(UserEntityFixture.get(userName, password)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(userName, title, body));
    }


    @Test
    void 포스트생성시_유저가_존재하지_않으면_에러를_내뱉는다() {
        String userName = "name";
        String password = "password";
        String title = "title";
        String body = "body";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class, () -> postService.create(userName, title, body));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

}