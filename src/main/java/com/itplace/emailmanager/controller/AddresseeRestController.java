package com.itplace.emailmanager.controller;

import com.itplace.emailmanager.domain.Addressee;
import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.dto.AddresseeDto;
import com.itplace.emailmanager.service.AddresseeService;
import com.itplace.emailmanager.service.MailService;
import com.itplace.emailmanager.util.AddresseeImportExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddresseeRestController extends RestApiController {

    @Autowired
    private MailService mailService;

    @Autowired
    private AddresseeService addresseeService;

    @Autowired
    private AddresseeImportExport importExport;

    @GetMapping("/addressee/{id}/mails")
    public List<Mail> getAddresseeMails(@PathVariable Long id) {
        return mailService.findByAddresseId(id);
    }

    @RequestMapping(value = "/addressee", method = RequestMethod.POST)
    public ResponseEntity addAddressee(@Validated @RequestBody AddresseeDto addressee) {

        Addressee created = addresseeService.save(addressee, Addressee.class);
        return new ResponseEntity<>(created != null ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping("/addressee/export")
    public ResponseEntity exportAddressees(@RequestBody String path) {
        return new ResponseEntity<>(importExport.exportToFile
                (addresseeService.findAll(), path) ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT);
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

    @RequestMapping(value = "/addressee/{id}", method = RequestMethod.PATCH)
    public ResponseEntity editAddressee(@PathVariable("id") Long id, @Validated @RequestBody() AddresseeDto source) {
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

}
