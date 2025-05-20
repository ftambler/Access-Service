package um.g7.Access_Service.Infrastructure;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {
    
    @Bean
    NewTopic userTopic() {
        return TopicBuilder.name("users").build();
    }

    @Bean
    NewTopic userVectorTopic() {
        return TopicBuilder.name("userVector").build();
    }
    
    @Bean
    NewTopic userRFIDTopic() {
        return TopicBuilder.name("userRFID").build();
    }
    
    @Bean
    NewTopic doorTopic() {
        return TopicBuilder.name("door").build();
    }
}
