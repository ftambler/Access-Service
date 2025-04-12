package um.g7.Access_Service.Infrastructure;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import um.g7.Access_Service.Domain.Entities.UserEntity;

@Component
public class UserTopicProducer {
    
    private final String USER_TOPIC = "users";
    
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserTopicProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void addUser(UserEntity user) throws JsonProcessingException {
        kafkaTemplate.send(USER_TOPIC, objectMapper.writeValueAsString(user));
    }

}
