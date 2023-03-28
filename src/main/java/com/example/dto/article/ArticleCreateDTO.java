package com.example.dto.article;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCreateDTO {

    private String id;
    @NotBlank
    @Size(min = 5, message = "Title is required")
    private String title;
    @NotBlank
    @Size(min = 5, message = "Description is required")
    private String description;
    @NotBlank
    @Size(min = 5, message = "Content is required")
    private String content;
    @NotBlank
    @Size(message = "Image id required")
    private String imageId;
    @Positive
    private Integer regionId;
    @Positive
    private Integer categoryId;


}