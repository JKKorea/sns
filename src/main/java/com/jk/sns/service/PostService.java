package com.jk.sns.service;

import com.jk.sns.exception.ErrorCode;
import com.jk.sns.exception.SimpleSnsApplicationException;
import com.jk.sns.model.entity.PostEntity;
import com.jk.sns.model.entity.UserEntity;
import com.jk.sns.repository.PostEntityRepository;
import com.jk.sns.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PostService {

    private final UserEntityRepository userEntityRepository;
    private final PostEntityRepository postEntityRepository;

    @Transactional
    public void create(String userName, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
            .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName)));

        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }
}
