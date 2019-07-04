package com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Department;
import com.itplace.emailmanager.repositry.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DepartmentService extends BaseRepository<DepartmentRepository, Department> {
    @Autowired
    AddresseeService addresseeService;

    @Transactional
    public Department createNewDepartment(Department department) {
        if (!repository.existsByNameEqualsIgnoreCase(department.getName())) return repository.save(department);
        else return null;
    }

    @Transactional
    public void deleteDepartmentAndAddresses(Department department) {
        List<Addressee> addressees = addresseeService.findByDepartmentId(department.getId());
        addressees.forEach(item -> addresseeService.delete(item));
        delete(department);
    }
}
