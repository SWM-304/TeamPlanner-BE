package com.tbfp.teamplannerbe.domain.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Random;

@Service
public class MailSenderService {

    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) throws Exception{
        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);*/

        /*
        String verificationNum = getVerificationNumber().toString();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);

        Resource resource = resourceLoader.getResource("classpath:static/acceptEmail.html");
        InputStream inputStream = resource.getInputStream();
        String emailTemplate = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        emailTemplate = emailTemplate.replace("{{CertificationNumber}}", verificationNum);

        helper.setText(emailTemplate, true);
        mailSender.send(mimeMessage);
        */

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        mailSender.send(mimeMessage);
    }

    public Integer getVerificationNumber() {
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
        Random r = new Random();
        Integer checkNum = r.nextInt(888888) + 111111;

        return checkNum;
    }

    public String getRandomPassword() {
        int PASSWORD_LENGTH = 12;
        String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890123456789!@#$%^&*";

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(PASSWORD_CHARACTERS.length());
            char randomChar = PASSWORD_CHARACTERS.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }
}