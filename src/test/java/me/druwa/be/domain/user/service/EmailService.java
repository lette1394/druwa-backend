package me.druwa.be.domain.user.service;

import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@Primary
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Builder(builderMethodName = "sendBuilder", buildMethodName = "send")
    @SneakyThrows
    public void send(final String to, final String subject, final String text) {
        // no op
    }
}
