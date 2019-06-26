package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Role.ROLE> {

}
