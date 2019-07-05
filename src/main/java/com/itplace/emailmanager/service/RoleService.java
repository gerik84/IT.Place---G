package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Role;
import com.itplace.emailmanager.repositry.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleService  {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }


}
