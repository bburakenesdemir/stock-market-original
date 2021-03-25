package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.data.User;
import com.burakenesdemir.stockmarket.exception.BadRequestException;
import com.burakenesdemir.stockmarket.util.ConfigLoaderUtil;
import com.burakenesdemir.stockmarket.type.MailAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.SEND_EMAIL_ERROR;

@Service
public class MailService {

    @Autowired
    MailClient mailClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public void sendUserRegisterActivationMail(User user, String subject, String activationLink) {
        try {
            String from = MailAttributes.MAIL_SENDER.getName() + "<" + ConfigLoaderUtil.getProperty("spring.mail.from.email") + ">";
            mailClient.prepareAndSendToRegisterActivationMail(user, from, subject, activationLink);

        } catch (MailException e) {
            LOGGER.info("{}: [ user registered but an error occured while sending user key, email: '{}' ]", MailService.class.getSimpleName(), user.getUserEmail());
            throw new BadRequestException(SEND_EMAIL_ERROR);
        }
    }

    public void sendActivationSuccessMail(User user, String subject) {
        try {
            String from = MailAttributes.MAIL_SENDER.getName() + "<" + ConfigLoaderUtil.getProperty("spring.mail.from.email") + ">";
            mailClient.prepareAndSendToRegisterActivationSuccessMail(user, from, subject);

        } catch (MailException e) {
            LOGGER.info("{}: [ user registered but an error occured while sending user key, email: '{}' ]", MailService.class.getSimpleName(), user.getUserEmail());
            throw new BadRequestException(SEND_EMAIL_ERROR);
        }
    }

    public void sendUserResetPasswordMail(User user, String resetPasswordUri,String subject){
        try {
            String from = MailAttributes.MAIL_SENDER.getName() + "<" + ConfigLoaderUtil.getProperty("spring.mail.from.email") + ">";
            mailClient.prepareAndSendToResetPassword(user, from, subject, resetPasswordUri);
        } catch (MailException e) {
            LOGGER.info("{}: [ user registered but an error occured while sending activation code, email: '{}' ]", MailService.class.getSimpleName(), user.getUserEmail());
            throw new BadRequestException(SEND_EMAIL_ERROR);
        }
    }
}