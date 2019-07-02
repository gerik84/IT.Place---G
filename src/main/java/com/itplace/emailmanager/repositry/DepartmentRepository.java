package com.itplace.emailmanager.repositry;

import com.itplace.emailmanager.domain.Department;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends LongJpaRepository<Department> {
    boolean existsByNameEqualsIgnoreCase(String name);

    Department findByNameEquals(String name);
}
