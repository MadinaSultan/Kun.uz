package com.example.dto.article;

import com.example.entity.ArticleEntity;
import com.example.entity.ProfileEntity;
import com.example.enums.ArticleLIkeStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleLikeDTO {
    private Integer id;
    private Integer profileId;
    private ProfileEntity profile;
    private String articleId;
    private ArticleEntity article;
    private LocalDateTime createdDate;
    private ArticleLIkeStatus status;

}
