package org.example.monitoringag;

import org.example.monitoringag.Service.AlertManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class mailTest {

    @Autowired
    private AlertManagerService alertManagerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmailAlertCpu() {

        String to ="seifeddine.benkarim@esprit.tn";

        alertManagerService.sendEmailAlertCpu(to);

        // Assert
        //Mockito.verify(javaMailSender, Mockito.times(1)).send(any(SimpleMailMessage.class));
    }

}
