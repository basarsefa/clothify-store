package com.sefa.clothify_store.service;

import com.sefa.clothify_store.exception.EmailMessagingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationEmail(String email, String verificationToken) {
        String subject = "Email Verification";
        String actionUrl = "http://localhost:3000/verify-email?token=" + verificationToken;
        String message = "Click the button below to verify your email adress";
        sendEmail(email,actionUrl, subject, message);
    }

    public void sendForgotPasswordEmail(String email, String resetToken) {
        String subject = "Reset Password";
        String actionUrl = "http://localhost:3000/reset-password?token=" + resetToken;
        String message = "Click the button below to change your password";
        sendEmail(email,actionUrl,subject,message);
    }

    @Async
    public  void sendEmail(String email, String actionUrl, String subject,String message) {
        try{

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: auto; border: 1px solid #eee; border-radius: 8px;">
                    <h2 style="color: #333; text-align: center;">%s</h2>
                    <p style="color: #555; font-size: 16px; text-align: center;">%s</p>
                    
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #4F46E5; color: white; padding: 12px 24px; text-decoration: none; font-size: 16px; font-weight: bold; border-radius: 5px; display: inline-block;">
                            İşlemi Tamamla
                        </a>
                    </div>
                    
                    <p style="color: #666; font-size: 12px; text-align: center;">
                        Bu linkin geçerlilik süresi <b>15 dakikadır</b>.<br>
                        Eğer buton çalışmıyorsa şu linki tarayıcınıza yapıştırabilirsiniz:<br>
                        <a href="%s" style="color: #4F46E5;">%s</a>
                    </p>
                </div>
                """.formatted(subject, message, actionUrl, actionUrl, actionUrl);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(htmlContent,true);
            mailSender.send(mimeMessage);
            log.info("Email sistem tarafından kullanıcıya gönderilmiştir. Subject: {}, Email:{}", subject, email);

        }catch (MessagingException e){
            throw new EmailMessagingException("E-posta gönderilemedi.");
        }catch (Exception e) {
            throw new RuntimeException("E-posta gönderilirken bilinmeyen bir hata oluştu.", e);
        }
    }
}
