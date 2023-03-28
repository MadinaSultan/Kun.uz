package com.example.dto.comment;

import com.example.entity.ArticleEntity;
import com.example.entity.CommentEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ArticleLIkeStatus;
import com.example.enums.CommentLikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeDTO {
    private Integer id;
    private Integer profileId;
    private ProfileEntity profile;
    private Integer commentId;
    private CommentEntity comment;
    private LocalDateTime createdDate;
    private CommentLikeStatus status;

}
