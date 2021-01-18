package com.interview.geobyte.service.serviceImpl;


import com.interview.geobyte.dto.NotificationEmail;
import com.interview.geobyte.exception.GeoByteAsyncExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail) throws GeoByteAsyncExceptionHandler {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setFrom(new InternetAddress("no-reply@geobyte.com", "GeoByte Delivery"));
            mimeMessageHelper.setReplyTo("no-reply@geobyte.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()), true);
        };

        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Mail sent!");
        }catch (MailException e){
            throw new GeoByteAsyncExceptionHandler(String.format("Exception occurred when sending mail to %s", notificationEmail.getRecipient()));
        }
    }
}
