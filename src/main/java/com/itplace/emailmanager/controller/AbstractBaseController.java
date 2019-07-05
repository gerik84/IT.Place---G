package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

abstract class AbstractBaseController {

    protected final static List<Role.ROLE> allow_all = new ArrayList<>();
    static {
        allow_all.add(Role.ROLE.ROLE_ADMIN);
        allow_all.add(Role.ROLE.ROLE_USER);
    }

    protected final static List<Role.ROLE> only_admin = new ArrayList<>();
    static {
        only_admin.add(Role.ROLE.ROLE_ADMIN);
    }

    protected final static List<Role.ROLE> only_user = new ArrayList<>();
    static {
        only_user.add(Role.ROLE.ROLE_USER);
    }

    protected boolean checkAccess(List<Role.ROLE> allow_roles) {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .noneMatch((Predicate<GrantedAuthority>) grantedAuthority -> allow_roles.stream()
                        .anyMatch(role -> grantedAuthority.getAuthority().equals(role.toString())));
    }
}
