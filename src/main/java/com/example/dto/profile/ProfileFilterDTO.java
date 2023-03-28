package com.example.dto.profile;

import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileFilterDTO {
    private Integer id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private LocalDate fromDate;
    private LocalDate toDate;

    private ProfileStatus status;
    private ProfileRole role;
    private Boolean visible;
}
