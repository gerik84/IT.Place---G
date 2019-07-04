package com.itplace.emailmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter

@JsonInclude(JsonInclude.Include.NON_NULL)
abstract public class BaseDto {

    private Long id;
    private Long whenCreated;
    private Long whenUpdated;
    private Long whenDeleted = null;

}
