package com.jk.sns.model;

import com.jk.sns.model.entity.LikeEntity;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Like {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static Like fromEntity(LikeEntity entity) {
        return new Like(
            entity.getId(),
            entity.getUser().getId(),
            entity.getUser().getUserName(),
            entity.getPost().getId(),
            entity.getRegisteredAt(),
            entity.getUpdatedAt(),
            entity.getRemovedAt()
        );
    }
}
