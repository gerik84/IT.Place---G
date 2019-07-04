package com.itplace.emailmanager.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itplace.emailmanager.domain.BaseEntity;
import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.dto.BaseDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
abstract public class RestApiController {

    protected final static List<Role.ROLE> allow_all = new ArrayList<>();
    static {
        allow_all.add(Role.ROLE.ROLE_ADMIN);
        allow_all.add(Role.ROLE.ROLE_USER);
    }

    protected final static List<Role.ROLE> only_admin = new ArrayList<>();
    static {
        allow_all.add(Role.ROLE.ROLE_ADMIN);
    }

    protected final static List<Role.ROLE> only_user = new ArrayList<>();
    static {
        allow_all.add(Role.ROLE.ROLE_USER);
    }

    protected ResponseEntity createResponse(Object body) {
        return body == null ?  new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK) :
                new ResponseEntity<>(body, HttpStatus.OK);
    }

    protected  <T extends BaseDto> String createMapper(BaseEntity model, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(new ModelMapper().map(model, clazz));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean checkAccess(List<Role.ROLE> allow_roles) {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(
                        (Predicate<GrantedAuthority>) grantedAuthority -> {
                            boolean isAllow = false;
                            for (Role.ROLE role : allow_roles) {
                                isAllow = grantedAuthority.getAuthority().equals(role.toString());

                                if (isAllow) {
                                    break;
                                }
                            }
                            return isAllow;
                        });
    }

}
