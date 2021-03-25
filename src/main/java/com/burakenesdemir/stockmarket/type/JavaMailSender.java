package com.burakenesdemir.stockmarket.type;

import com.burakenesdemir.stockmarket.util.ConfigLoaderUtil;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

public class JavaMailSender {
    private static final String host = "smtp.gmail.com";
    public static org.springframework.mail.javamail.JavaMailSender getJavaMailSender()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(ConfigLoaderUtil.getProperty("spring.mail.port")));
        mailSender.setDefaultEncoding(ConfigLoaderUtil.getProperty("spring.mail.default-encoding"));
        mailSender.setUsername(ConfigLoaderUtil.getProperty("spring.mail.from.email"));
        mailSender.setPassword(ConfigLoaderUtil.getProperty("spring.mail.from.password"));
        mailSender.setProtocol("smtp");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.port", Integer.parseInt(ConfigLoaderUtil.getProperty("spring.mail.port")));
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", ConfigLoaderUtil.getProperty("spring.mail.from.email"));
        props.put("mail.smtp.password", ConfigLoaderUtil.getProperty("spring.mail.from.password"));

        return mailSender;
    }
}