package com.jk.sns.repository;

import com.jk.sns.model.entity.PostEntity;
import com.jk.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {

    public Page<PostEntity> findAllByUser(UserEntity userEntity, Pageable pageable);
}
