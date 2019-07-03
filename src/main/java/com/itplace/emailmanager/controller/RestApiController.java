package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.*;
import com.itplace.emailmanager.security.UserDetails.UserDetailsImpl;
import com.itplace.emailmanager.service.*;
import com.itplace.emailmanager.util.AddresseeImportExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {
    @Autowired
    AddresseeService addresseeService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    MailTaskService mailTaskService;
    @Autowired
    MailService mailService;
    @Autowired
    SenderService senderService;
    @Autowired
    AddresseeImportExport addresseeImportExport;
    @Autowired
    AddresseeImportExport importExport;

    @GetMapping("/mails")
    public List<Mail> getMailsWithSenderId(@RequestParam(value = "first", required = false) Integer _first,
                                           @RequestParam(value = "max", required = false) Integer _max,
                                           @RequestParam(value = "sort", required = false) String _sort,
                                           @RequestParam(value = "direction", required = false) String _direction) {

        Long sender_id = senderService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        return mailService.findByAll(sender_id, _first, _max, _sort, _direction);
    }

    @GetMapping("/mails/page/{pageNo}/{pageSize}/{sorted}")
    public List<Mail> getMailsByPageNo(@PathVariable int pageNo, @PathVariable int pageSize, @PathVariable String sorted){
        return mailService.findAll(pageNo, pageSize, sorted, null);
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public ResponseEntity getDepartment() {
        return createResponse(departmentService.findAll());
    }

    @RequestMapping(value = "/department/{id}/addressees", method = RequestMethod.GET)
    public ResponseEntity getAddressee(@PathVariable("id") Long department) {
        return createResponse(addresseeService.findByDepartmentId(department));
    }

    @GetMapping("/addressee/{id}/mails")
    public List<Mail> getAddresseeMails(@PathVariable Long id){
        return mailService.findByAddresseId(id);
    }

    @GetMapping("/senders")
    public ResponseEntity getSenders(){
        return createResponse(senderService.findAll());
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public ResponseEntity saveMail(@RequestBody Mail mail){
        mail.setSender(senderService.findByEmail
                (SecurityContextHolder.getContext().getAuthentication().getName()));
        Mail created = mailService.createNewMail(mail);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/addressee", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@RequestBody Addressee addressee){
        Addressee created = addresseeService.createNewAddressee(addressee);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@RequestBody Department department){
        Department created = departmentService.createNewDepartment(department);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/sender", method = RequestMethod.POST)
    public ResponseEntity addSender(@RequestBody Sender sender){
        Sender created = senderService.save(sender);

        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value="/user", method = RequestMethod.PATCH)
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl currentUser, @RequestBody String password){
        senderService.changePassword(currentUser, password);
    }

    private ResponseEntity createResponse(Object body) {
        return body == null ?  new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK) :
                new ResponseEntity<>(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/mail/{id}", method = RequestMethod.GET)
    public Mail getMail(@PathVariable("id") Long id) {
        return mailService.findById(id);
    }

    @RequestMapping(value="/mail/{id}", method = RequestMethod.PATCH)
    public ResponseEntity changeMail(@PathVariable("id") Long id,  @RequestBody Mail mailIn) {
        Mail mail = mailService.findById(id);
        if (mail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Mail save = mailService.changeMailStatus(mail, mailIn.getStatus());
        return new ResponseEntity<>(save != null ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT);
    }

    @RequestMapping(value="/mail/{id}/task/status/{status}", method = RequestMethod.PATCH)
    public ResponseEntity changeTaskStatus(@PathVariable("id") Long id, @PathVariable("status") MailTask.STATUS status) {
        Mail mail = mailService.findById(id);
        if (mail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            mail.getMailTask().setStatus(status);
            mailService.save(mail);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/addressee/export")
    public ResponseEntity exportAddressees(@RequestBody String path){
        return new ResponseEntity<>(addresseeImportExport.exportToFile
                (addresseeService.findAll(), path) ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT);
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

    @RequestMapping(value = "/addressee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAddressee(@PathVariable("id") Long id) {
        Addressee addressee = addresseeService.findById(id);
        if (addressee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            addresseeService.delete(addressee);
            return new ResponseEntity<>(HttpStatus.OK);
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

    @RequestMapping(value = "/addressee/{id}", method = RequestMethod.PATCH)
    public ResponseEntity editAddressee(@PathVariable("id") Long id, @RequestBody() Addressee source) {
        Addressee dest = addresseeService.findById(id);
        if (dest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            dest.setName(source.getName());
            dest.setEmail(source.getEmail());
//            dest.setDepartment(source.getDepartment());
            addresseeService.save(dest);
            return new ResponseEntity<>(dest, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @RequestMapping(value = "/department/{id}/addressee", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@PathVariable("id") Long department_id, @RequestBody() Addressee source) {
        Department department = departmentService.findById(department_id);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            source.setDepartment(department);
            addresseeService.save(source);
            return new ResponseEntity<>(source, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

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

}
