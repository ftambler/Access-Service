package um.g7.Access_Service.Domain.Services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import um.g7.Access_Service.Domain.Entities.Admin;

@Service
public class EmailService {

    @Value("${aws.ses.configSet}")
    private String CONFIGSET;

    @Value("${aws.ses.senderEmail}")
    private String FROM;

    private final AmazonSimpleEmailService client;
    private final AdminService adminService;

    public EmailService(AdminService adminService, AmazonSimpleEmailService amazonSimpleEmailService) {
        this.client = amazonSimpleEmailService;
        this.adminService = adminService;
    }

    public void sendEmail(String doorName, long time, String accessType, int failedStreak) {
        String SUBJECT = buildSubject(doorName);
        String HTMLBODY = buildMailContentHTML(doorName, time, accessType, failedStreak);
        String TEXTBODY = buildMailContentText(doorName, time, accessType, failedStreak);

        SendEmailRequest request = new SendEmailRequest()
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content().withCharset("UTF-8").withData(HTMLBODY))
                                .withText(new Content().withCharset("UTF-8").withData(TEXTBODY)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM)
                .withConfigurationSetName(CONFIGSET);

        List<Admin> adminList = adminService.getAllAdmins();
        for (Admin ad : adminList) {
            System.out.println("Sending to: " + ad.getEmail());

            try {
                this.client.sendEmail(request.withDestination(new Destination().withToAddresses(ad.getEmail())));
                System.out.println("Email sent to" + ad.getEmail());
            } catch (Exception e) {
                System.out.println("Couldnt send email to: " + ad.getEmail());
            }

        }

    }

    private String buildSubject(String doorName) {
        return "Too Many Access Failed Reported on: " + doorName;
    }

    private String buildMailContentHTML(String doorName, long time, String accessType, int failedStreak) {
        return "<html><body style='font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; color: #333;'>"
                + "<h2 style='color: #d9534f;'>🚨 Multiple Accesses Failed</h2>"
                + "<p><strong>Attempts:</strong> " + failedStreak + "<br>"
                + "<strong>Door:</strong> " + doorName + "<br>"
                + "<strong>Time:</strong> " + new Date(time).toString() + "</p>"
                + "</body></html>";
    }

    private String buildMailContentText(String doorName, long time, String accessType, int failedStreak) {
        return "Multiple accesses failed (" + failedStreak + ") on: " + doorName + " at: "
                + (new Date(time)).toString();
    }
}
