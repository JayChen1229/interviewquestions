package com.example.engineerpracticequestions.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@NamedStoredProcedureQueries({@NamedStoredProcedureQuery(
        name = "FindUserById",
        procedureName = "FindUserById",
        resultClasses = User.class,
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Integer.class)
        }
),
        @NamedStoredProcedureQuery(
                name = "SaveOrUpdateUser",
                procedureName = "SaveOrUpdateUser",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_name", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_password", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cover_image", type = byte[].class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_biography", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "FindUserByEmail",
                procedureName = "FindUserByEmail",
                resultClasses = User.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_email", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "UpdateUserCoverImage",
                procedureName = "UpdateUserCoverImage",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_cover_image", type = byte[].class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "EmailExists",
                procedureName = "EmailExists",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_email", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_exists", type = Integer.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "FindUserByPostId",
                procedureName = "FindUserByPostId",
                resultClasses = User.class,
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_post_id", type = Integer.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "FindUserByCommentId",
                procedureName = "FindUserByCommentId",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_comment_id", type = Long.class)
                },
                resultClasses = {User.class}
        ),
        @NamedStoredProcedureQuery(
                name = "UpdateUserBiography",
                procedureName = "UpdateUserBiography",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_user_id", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_biography", type = String.class)
                }
        )

})
@Table(name = "user")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String email;
    private String password; // Hashed password
    private byte[] coverImage;
    private String biography;

    @Transient
    private String imgUrl;
}

