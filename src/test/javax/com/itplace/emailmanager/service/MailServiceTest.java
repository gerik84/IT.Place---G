package javax.com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailRepository;
import com.itplace.emailmanager.repositry.MailTaskRepository;
import com.itplace.emailmanager.service.MailService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {
    @InjectMocks
    MailService mailService;

    @Test
    public void createNewMailTest(){
        Mail someMail = new Mail();
        someMail.setSubject("Test");

//        MailTask expectedTask = new MailTask();
//        doReturn(expectedTask).when(mailTaskRepository).save(expectedTask);

//        Mail expectedMail = mailService.createNewMail(someMail);
//        Mail expectedMail = mailService.save(someMail);

//        assertThat(expectedMail.getSubject()).isEqualTo("Test");
    }
}
