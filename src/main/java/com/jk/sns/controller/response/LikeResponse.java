package com.jk.sns.controller.response;

import com.jk.sns.model.Like;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static LikeResponse fromLike(Like like) {
        return new LikeResponse(
            like.getId(),
            like.getUserId(),
            like.getUserName(),
            like.getPostId(),
            like.getRegisteredAt(),
            like.getUpdatedAt(),
            like.getRemovedAt()
        );
    }
}
