package com.jk.sns.model;

import com.jk.sns.model.entity.PostEntity;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

    private Integer id = null;

    private String title;

    private String body;

    private User user;

    private List<Comment> comments;
    private List<Like> likes;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp removedAt;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
            entity.getId(),
            entity.getTitle(),
            entity.getBody(),
            User.fromEntity(entity.getUser()),
            entity.getComments().stream().map(Comment::fromEntity).collect(Collectors.toList()),
            entity.getLikes().stream().map(Like::fromEntity).collect(Collectors.toList()),
            entity.getRegisteredAt(),
            entity.getUpdatedAt(),
            entity.getRemovedAt()
        );
    }
}
