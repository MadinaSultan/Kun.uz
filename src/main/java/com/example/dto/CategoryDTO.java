package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Integer id;
    @NotBlank
    @Size(min = 5,message = "Key name is required")
    private String key;
    @NotBlank
    @Size(min = 5,message = "Name uzbek name is required")
    private String nameUz;
    @NotBlank
    @Size(min = 5,message = "Name russian name is required")
    private String nameRu;
    @NotBlank
    @Size(min = 5,message = "Name english name is required")
    private String nameEng;
    private Integer prtId;

    private Boolean visible;
    @NotBlank
    @Size(min = 5,message = "Name is required")
    private String name;
    private LocalDateTime createdDate;

}
