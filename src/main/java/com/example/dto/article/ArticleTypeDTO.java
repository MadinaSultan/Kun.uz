package com.example.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SortComparator;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleTypeDTO {
    private Integer id;
    @NotBlank
    @Size(min = 5, message = "Key is required")
    private String key;


    private Integer prtId;
    @NotBlank
    @Size(min = 5, message = "Name Uz is required")

    private String nameUz;
    @NotBlank
    @Size(min = 5, message = "Name Ru is required")

    private String nameRu;
    @NotBlank
    @Size(min = 5, message = "Name Eng is required")

    private String nameEn;
    @NotBlank
    @Size(min = 5, message = "Name  is required")

    private String name;

    private LocalDateTime createdDate;
}
