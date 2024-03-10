package com.jk.sns.model;

import com.jk.sns.model.entity.PostEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

    private Integer id = null;

    private String title;

    private String body;

    private User user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
            entity.getId(),
            entity.getTitle(),
            entity.getBody(),
            User.fromEntity(entity.getUser()),
            entity.getRegisteredAt(),
            entity.getUpdatedAt(),
            entity.getRemovedAt()
        );
    }
}
