package um.g7.Access_Service.Infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

import um.g7.Access_Service.Domain.Services.AdminService;

@Configuration
public class AwsConfig {

    @Value("${aws.ses.accessKey}")
    private String accessKey;

    @Value("${aws.ses.secretKey}")
    private String secretKey;

    @Bean
    public AmazonSimpleEmailService EmailService(AdminService adminService) {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(Regions.US_EAST_2)
                .build();
    }
}
