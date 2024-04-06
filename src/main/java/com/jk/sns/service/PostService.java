package com.jk.sns.service;

import com.jk.sns.exception.ErrorCode;
import com.jk.sns.exception.SimpleSnsApplicationException;
import com.jk.sns.model.AlarmArgs;
import com.jk.sns.model.AlarmType;
import com.jk.sns.model.Post;
import com.jk.sns.model.entity.AlarmEntity;
import com.jk.sns.model.entity.CommentEntity;
import com.jk.sns.model.entity.LikeEntity;
import com.jk.sns.model.entity.PostEntity;
import com.jk.sns.model.entity.UserEntity;
import com.jk.sns.repository.AlarmEntityRepository;
import com.jk.sns.repository.CommentEntityRepository;
import com.jk.sns.repository.LikeEntityRepository;
import com.jk.sns.repository.PostEntityRepository;
import com.jk.sns.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    @Transactional
    public void create(String userName, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));

        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    // entity mapping
    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));
        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public Post modify(String userName, Integer postId, String title, String body) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(
            () -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("postId is %d", postId)));

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));

        if (postEntity.getUser() != userEntity) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION,
                String.format("user %s has no permission with post %d", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.save(postEntity));
    }

    @Transactional
    public void delete(String userName, Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(
            () -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("postId is %d", postId)));

        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));

        if (postEntity.getUser() != userEntity) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION,
                String.format("user %s has no permission with post %d", userName, postId));
        }
        postEntityRepository.delete(postEntity);
    }

    @Transactional
    public void comment(Integer postId, String userName, String comment) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(
            () -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));

        commentEntityRepository.save(CommentEntity.of(comment, postEntity, userEntity));

        // create alarm
        alarmEntityRepository.save(
            AlarmEntity.of(AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(userEntity.getId(), postId),
                postEntity.getUser()));
    }

    @Transactional
    public void like(Integer postId, String userName) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(
            () -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("postId is %d", postId)));
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.ALREADY_LIKED_POST,
                String.format("userName %s already like the post %s", userName, postId));
        });

        likeEntityRepository.save(LikeEntity.of(postEntity, userEntity));

        // create alarm
        alarmEntityRepository.save(
            AlarmEntity.of(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(userEntity.getId(), postId),
                postEntity.getUser()));

    }
}
