package org.example.monitoringag.Service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
public class AlertManagerService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmailAlertCpu(String to) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Cpu critical health");
            message.setText("CPU usage above 90% , considered critical. This indicates that the system is under severe load");
            javaMailSender.send(message);
    }

    public void sendEmailAlertMemoire(String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Memory critical health");
        message.setText("Memory usage above 90% , considered critical. This indicates that the system is under severe load");
        javaMailSender.send(message);
    }


}
