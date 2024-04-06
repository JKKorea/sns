package com.jk.sns.controller.response;

import com.jk.sns.model.Comment;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Integer id;
    private String comment;
    private Integer userId;
    private String userName;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getComment(),
            comment.getUserId(),
            comment.getUserName(),
            comment.getPostId(),
            comment.getRegisteredAt(),
            comment.getUpdatedAt(),
            comment.getRemovedAt()
        );
    }
}
