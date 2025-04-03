package com.foro.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.foro.web.model.Contacto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class ContactoEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoVerificacion(Contacto contacto) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setFrom("forowebMVC@foroul.com");  // Cambia a una dirección válida
            helper.setTo(contacto.getUsuario().getUsuario());
            helper.setSubject("Confirmación de Envío de Reporte");

            String contenidoHtml = "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                    + "<h2 style='color: #4CAF50; text-align: center;'>Confirmación de Envío de Reporte</h2>"
                    + "<p>Estimado usuario, " +contacto.getUsuario().getNombres()+" "+contacto.getUsuario().getApellidos()+""
                    + "<p>Hemos recibido una solicitud para enviar un reporte utilizando su correo electrónico. "
                    + "Para confirmar y proceder con el envío, por favor haga clic en el botón de abajo.</p>"
                    + "<p style='text-align: center; margin: 20px 0;'>"
                    + "<a href='http://localhost:8080/verificacion?id=" + contacto.getId() + "&respuesta=si' "
                    + "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #4CAF50; text-decoration: none; border-radius: 5px;'>"
                    + "Sí, acepto</a>"
                    + "</p>"
                    + "<p>Si usted no realizó esta solicitud, ignore este mensaje. No se enviará ningún reporte a menos que confirme.</p>"
                    + "<p style='margin-top: 30px;'>Atentamente,<br>El equipo de ForoUTP</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(contenidoHtml, true); // true indica que es HTML
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Manejar el error de envío
        }
    }
}
