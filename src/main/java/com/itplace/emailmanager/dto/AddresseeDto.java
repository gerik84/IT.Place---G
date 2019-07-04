package com.itplace.emailmanager.dto;

import com.itplace.emailmanager.domain.Department;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class AddresseeDto extends BaseDto {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    private Department department;


}
