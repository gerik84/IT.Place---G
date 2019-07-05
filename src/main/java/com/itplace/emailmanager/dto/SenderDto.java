package com.itplace.emailmanager.dto;

import com.itplace.emailmanager.domain.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter

public class SenderDto extends BaseDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String emailPassword;

    private Integer port = 465;

    @NotNull
    private String smtp;

    @NotNull
    private Role role;

}
