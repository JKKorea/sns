package com.jk.sns.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jk.sns.exception.ErrorCode;
import com.jk.sns.exception.SimpleSnsApplicationException;
import com.jk.sns.fixture.UserEntityFixture;
import com.jk.sns.model.entity.PostEntity;
import com.jk.sns.model.entity.UserEntity;
import com.jk.sns.repository.PostEntityRepository;
import com.jk.sns.repository.UserEntityRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

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
        String title = "title";
        String body = "body";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class, () -> postService.create(userName, title, body));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 내_포스트리스트를_가져올_유저가_존재하지_않으면_에러를_내뱉는다() {
        String userName = "name";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class,
            () -> postService.my(userName, mock(Pageable.class)));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_포스트가_존재하지_않으면_에러를_내뱉는다() {
        Integer postId = 1;
        String userName = "name";
        String title = "title";
        String body = "body";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class,
            () -> postService.modify(userName, postId, title, body));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_유저가_존재하지_않으면_에러를_내뱉는다() {
        Integer postId = 1;
        String userName = "name";
        String title = "title";
        String body = "body";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class,
            () -> postService.modify(userName, postId, title, body));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 포스트_수정시_포스트_작성자와_유저가_일치하지_않으면_에러를_내뱉는다() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        Integer postId = 1;
        String userName = "name";
        String title = "title";
        String body = "body";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));

        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class,
            () -> postService.modify(userName, postId, title, body));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void 포스트_삭제시_포스트가_존재하지_않으면_에러를_내뱉는다() {
        Integer postId = 1;
        String userName = "name";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class, () -> postService.delete(userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_삭제시_유저가_존재하지_않으면_에러를_내뱉는다() {
        Integer postId = 1;
        String userName = "name";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class, () -> postService.delete(userName, postId));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 포스트_삭제시_포스트_작성자와_유저가_일치하지_않으면_에러를_내뱉는다() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        Integer postId = 1;
        String userName = "name";
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        
        SimpleSnsApplicationException exception = Assertions.assertThrows(
            SimpleSnsApplicationException.class, () -> postService.delete(userName, postId));

        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

}
