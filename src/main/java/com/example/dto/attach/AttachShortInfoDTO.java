package com.example.dto.attach;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
@Setter
@NoArgsConstructor
public class AttachShortInfoDTO {
    private String id;
    private String url;

}
