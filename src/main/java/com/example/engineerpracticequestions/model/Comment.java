package com.example.engineerpracticequestions.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "FindCommentsByPostId",
                procedureName = "FindCommentsByPostId",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_post_id", type = Long.class)
                },
                resultClasses = {Comment.class}
        ),
        @NamedStoredProcedureQuery(
                name = "SaveOrUpdateComment",
                procedureName = "SaveOrUpdateComment",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "p_comment_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_post_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_content", type = String.class)
                },
                resultClasses = {Comment.class}
        )
})
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private Long postId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

    @Transient
    private User user;

    @Transient
    private Post post;
}
