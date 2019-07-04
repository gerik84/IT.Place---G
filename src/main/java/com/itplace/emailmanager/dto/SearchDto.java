package com.itplace.emailmanager.dto;

import com.itplace.emailmanager.domain.Addressee;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter

public class SearchDto extends BaseDto {

    private String name;

    private HashSet<Addressee> addressees;


}
