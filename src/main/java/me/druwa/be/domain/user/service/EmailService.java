package me.druwa.be.domain.user.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.druwa.be.domain.auth.service.TokenProvider;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Builder(builderMethodName = "sendBuilder", buildMethodName = "send")
    @SneakyThrows
    public void sendBuilder(final String to, final String subject, final String text) {
        final MimeMessage msg = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setFrom("no_reply@d-studio.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false);

        javaMailSender.send(msg);
    }
}
