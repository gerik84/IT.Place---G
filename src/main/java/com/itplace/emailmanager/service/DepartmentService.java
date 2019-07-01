package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Department;
import com.itplace.emailmanager.repositry.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends BaseRepository<DepartmentRepository, Department> {

    public Department saveNewDepartment(Department department) {
        if (!repository.existsByNameEqualsIgnoreCase(department.getName())) return repository.save(department);
        else return null;
    }
}
