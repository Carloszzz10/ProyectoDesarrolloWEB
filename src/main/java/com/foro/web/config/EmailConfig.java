/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {
@Value("${email.username}")
private String email;


@Value("${email.password}")
private String  password;

private Properties getmaiProperties (){
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.ssl.enable", "true"); 
    properties.put("mail.smtp.host", "mail.foroul.com");
    properties.put("mail.smtp.port", "465"); 
    properties.put("mail.smtp.socketFactory.port", "465"); 
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    return properties;
}
@Bean
public JavaMailSender javaMailSender(){
    JavaMailSenderImpl mailSender =  new JavaMailSenderImpl();
    mailSender.setJavaMailProperties(getmaiProperties());
    mailSender.setUsername(email);
    mailSender.setPassword(password);
    return mailSender;
            
            }

public ResourceLoader resourceLoader(){
    return new DefaultResourceLoader();
}

}
