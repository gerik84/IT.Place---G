package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Department;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends UuidJpaRepository<Department> {
}