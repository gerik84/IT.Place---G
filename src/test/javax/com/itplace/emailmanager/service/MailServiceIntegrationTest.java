package javax.com.itplace.emailmanager.service;

import com.itplace.emailmanager.domain.Mail;
import com.itplace.emailmanager.domain.MailTask;
import com.itplace.emailmanager.repositry.MailLogRepository;
import com.itplace.emailmanager.repositry.MailRepository;
import com.itplace.emailmanager.repositry.MailTaskRepository;
import com.itplace.emailmanager.service.MailLogService;
import com.itplace.emailmanager.service.MailService;
import com.itplace.emailmanager.service.MailTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestComponent
public class MailServiceIntegrationTest {
    @MockBean
    MailService mailService;
    @MockBean
    MailRepository mailRepository;
    @MockBean
    MailTaskService mailTaskService;

    List<Mail> testList;

    @Before
    public void setUp() throws Exception {
        testList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Mail mail = new Mail();
            if (i == 5) mail.setStatus(Mail.STATUS.SENDING);
            MailTask mailTask = new MailTask();
            mailTask.setStartTime(System.currentTimeMillis() - 1000);
            mail.setMailTask(mailTask);
            mail.setSubject(String.valueOf(i));
            mailRepository.save(mail);
            testList.add(mail);
        }
    }

    @Test
    public void findMailToSend() {
//        assertEquals(9, mailService.findMailToSend().size());
    }

    @Test
    public void createNewMail() {
    }

    @Test
    public void changeMailStatus() {
    }

    @Test
    public void changeMailStatus1() {
    }

    @Test
    public void findBySubjectLike() {
    }

    @Test
    public void findBySubject() {
    }

    @Test
    public void findByAddresseId() {
    }

    @Test
    public void findByAll() {
    }
}