package com.jk.sns.service;

import com.jk.sns.exception.ErrorCode;
import com.jk.sns.exception.SimpleSnsApplicationException;
import com.jk.sns.model.Alarm;
import com.jk.sns.model.User;
import com.jk.sns.model.entity.UserEntity;
import com.jk.sns.repository.AlarmEntityRepository;
import com.jk.sns.repository.UserEntityRepository;
import com.jk.sns.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
            () -> new SimpleSnsApplicationException(
                ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName))
        );
    }

    public String login(String userName, String password) {
        User savedUser = loadUserByUsername(userName);
        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtTokenUtils.generateAccessToken(userName, secretKey, expiredTimeMs);
    }


    @Transactional
    public User join(String userName, String password) {
        // check the userId not exist
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
                String.format("userName is %s", userName));
        });

        UserEntity savedUser = userRepository.save(
            UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(savedUser);
    }

    @Transactional
    public Page<Alarm> alarmList(String userName, Pageable pageable) {
        UserEntity user = userRepository.findByUserName(userName).orElseThrow(
            () -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("userName is %s", userName))
        );
        return alarmEntityRepository.findAllByUser(user, pageable).map(Alarm::fromEntity);
    }
}
