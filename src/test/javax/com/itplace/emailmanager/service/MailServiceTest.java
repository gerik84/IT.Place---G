package javax.com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.service.MailService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {
    @InjectMocks
    MailService mailService;

    @Test
    public void createNewMailTest(){
        Mail someMail = new Mail();
        someMail.setSubject("Test");
//        Mail expectedMail = mailService.createNewMail(someMail);

//        assertThat(expectedMail.getMailTask()).isNotNull();
    }
}
