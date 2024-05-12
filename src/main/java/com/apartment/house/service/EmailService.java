package com.apartment.house.service;

import com.apartment.house.config.ApplicationConfig;
import com.apartment.house.enums.EmailTemplateNameEnum;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;


@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final ApplicationConfig applicationConfig;
  private final SpringTemplateEngine templateEngine;


  @Async
  public void sendEmail(String to, String username, EmailTemplateNameEnum emailTemplateName,
      String confirmationUrl, String activationCode, String subject) throws MessagingException {
    String templateName =
        !emailTemplateName.getValue().isEmpty() ? emailTemplateName.getValue() : "confirm-email";

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                                                     MimeMessageHelper.MULTIPART_MODE_MIXED,
                                                     UTF_8.name()
    );

    Map<String, Object> model = new HashMap<>();
    model.put("username", username);
    model.put("confirmationUrl", confirmationUrl);
    model.put("activationCode", activationCode);

    Context context = new Context();
    context.setVariables(model);

    helper.setFrom(applicationConfig.mailFrom);
    helper.setTo(to);
    helper.setSubject(subject);

    String template = templateEngine.process(templateName, context);

    helper.setText(template, true);

    mailSender.send(mimeMessage);
  }
}