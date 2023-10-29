package com.segurosbolivar.sanyuschedulingapp.service.client;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.resource.Email;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class MailSenderService {

    @Value("${mailjet.api-key}")
    private String apiKey;
    @Value("${mailjet.secret-key}")
    private String secretKey;
    @Value("${mailjet.from-email}")
    private String fromEmail;

    public void sendEmailWithCSVAttachment(
            List<String> toEmails,
            String subject,
            String textPart,
            String csvFileName,
            byte[] cvsFile
    ) {

        try {

            MailjetClient client = new MailjetClient(ClientOptions
                    .builder()
                    .apiKey(apiKey)
                    .apiSecretKey(secretKey)
                    .build());

            for (String toEmail : toEmails) {

                MailjetRequest emailRequest = new MailjetRequest(Email.resource)
                        .property(Email.FROMNAME, "SANYU Scheduling App")
                        .property(Email.FROMEMAIL, fromEmail)
                        .property(Email.SUBJECT, subject)
                        .property(Email.TO, toEmail)
                        .property(Email.TEXTPART, textPart)
                        .property(Email.ATTACHMENTS, new JSONArray().put(
                                new JSONObject()
                                    .put("Content-Type", "text/csv")
                                    .put("Filename", csvFileName)
                                    .put("content", Base64.getEncoder().encodeToString(cvsFile)))
                        );

                client.post(emailRequest);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}