/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.foro.web.services;


import com.foro.web.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Carlos Abad
 */
@Service
public class EmailServiceImpl implements IEmailServices {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${email.username}") 
    private String senderEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void enviarcorreo(EmailDTO email) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email.getDestinatario());
            helper.setFrom(senderEmail); 
            helper.setSubject(email.getAsunto());

           
            Context context = new Context();
            context.setVariable("mensaje", email.getMensaje());
            String contenidoHTML = templateEngine.process("email", context);

       
            helper.setText(contenidoHTML, true);

         System.out.println("Enviando correo a " + email.getDestinatario());
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo electrónico: " + e.getMessage(), e);
        }
    }
}
