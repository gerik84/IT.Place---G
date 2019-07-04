package com.itplace.emailmanager.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itplace.emailmanager.domain.BaseEntity;
import com.itplace.emailmanager.dto.BaseDto;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.AddresseeImportExport;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class RestApiController {

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



}
