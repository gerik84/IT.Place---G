package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Department;
import com.itplace.emailmanager.dto.AddresseeDto;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.AddresseeImportExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@RestController
public class DepartmentRestController extends RestApiController {

    @Autowired
    private AddresseeService addresseeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AddresseeImportExport importExport;

    @Transactional
    @RequestMapping(value = "/department/{id}/csv/file", method = RequestMethod.POST)
    public ResponseEntity handleFileUpload(@PathVariable("id") Long department_id, @RequestParam("file") MultipartFile file) {
        Department department = departmentService.findById(department_id);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }



        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            List<Addressee> addressees = importExport.importFromReader(reader);
            addressees.forEach(addressee -> {
                addressee.setDepartment(department);
                addresseeService.save(addressee);
            });
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/department/{id}/addressee", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@PathVariable("id") Long department_id, @Validated @RequestBody() AddresseeDto source) {
        Department department = departmentService.findById(department_id);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        source.setDepartment(department);

        try {
            source.setDepartment(department);
            Addressee save = addresseeService.save(source, Addressee.class);
            return new ResponseEntity<>(createMapper(save), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.PATCH)
    public ResponseEntity editDepartment(@PathVariable("id") Long id, @RequestBody() Department source) {
        Department dest = departmentService.findById(id);
        if (dest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            dest.setName(source.getName());
            departmentService.save(dest);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteDepartment(@PathVariable("id") Long id) {
        Department department = departmentService.findById(id);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            departmentService.deleteDepartmentAndAddresses(department);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@RequestBody Department department){
        Department created = departmentService.createNewDepartment(department);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public ResponseEntity getDepartment() {
        return createResponse(departmentService.findAll());
    }

    @RequestMapping(value = "/department/{id}/addressees", method = RequestMethod.GET)
    public ResponseEntity getAddressee(@PathVariable("id") Long department) {
        return createResponse(addresseeService.findByDepartmentId(department));
    }

}
