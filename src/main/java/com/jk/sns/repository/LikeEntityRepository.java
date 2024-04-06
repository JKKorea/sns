package com.jk.sns.repository;

import com.jk.sns.model.entity.LikeEntity;
import com.jk.sns.model.entity.PostEntity;
import com.jk.sns.model.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);


}
