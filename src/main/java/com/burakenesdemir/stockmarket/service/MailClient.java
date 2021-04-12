package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.data.User;
import com.burakenesdemir.stockmarket.exception.BadRequestException;
import com.burakenesdemir.stockmarket.type.JavaMailSender;
import com.burakenesdemir.stockmarket.type.MailTemplates;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.SEND_EMAIL_ERROR;

@Service
@AllArgsConstructor
public class MailClient {


    private final MailContentBuilder mailContentBuilder;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailClient.class);

    public void prepareAndSendToRegisterActivationMail(User user, String from, String subject, String activationLink) {
        var params = new HashMap<String,String>();
        params.put("activationEmailCodeUri", activationLink);
        var message = getMimeMessagePreparatory(user, from, subject, params, MailTemplates.REGISTER_ACTIVATION_MAIL.getName());
        sendMail(message);
    }

    public void prepareAndSendToRegisterActivationSuccessMail(User user, String from, String subject) {
        var params = new HashMap<String,String>();
        params.put("userName", user.getUsername());
        var message = getMimeMessagePreparatory(user, from, subject, params, MailTemplates.ACTIVATION_SUCCESS.getName());
        sendMail(message);
    }

    public void prepareAndSendToResetPassword(User user, String from, String subject, String resetPasswordUri) {
        var params = new HashMap<String,String>();
        params.put("resetPasswordUri", resetPasswordUri);
        var message = getMimeMessagePreparatory(user, from, subject, params,MailTemplates.RESET_PASSWORD.getName());
        sendMail(message);
    }

    private <T> MimeMessagePreparator getMimeMessagePreparatory(User user, String from, String subject, Map<String,T> map, String templateType) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(from);
            messageHelper.setTo(user.getName() + "<" + user.getUserEmail() + ">");
            messageHelper.setSubject(subject);
            String content = mailContentBuilder.buildTemplate(map, templateType);
            messageHelper.setText(content, true);
        };
    }

    private void sendMail(MimeMessagePreparator message) {
        try {
            JavaMailSender.getJavaMailSender().send(message);
        } catch (Exception e) {
            LOGGER.error("Mail Send Exception ");
            throw new BadRequestException(SEND_EMAIL_ERROR);
        }
    }
}