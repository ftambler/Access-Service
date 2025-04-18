package um.g7.Access_Service.Infrastructure.KafkaProducers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserRFID;
import um.g7.Access_Service.Domain.Entities.UserVector;

@Component
public class UserTopicProducer {
    
    private final String USER_TOPIC = "users";
    private final String USER_VECTOR_TOPIC = "userVector";
    private final String USER_RFID_TOPIC = "userRFID";
    
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserTopicProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void addUser(UserEntity user) throws JsonProcessingException {
        kafkaTemplate.send(USER_TOPIC, objectMapper.writeValueAsString(user));
    }

    public void addVectorToUser(UserVector userVector) throws JsonProcessingException {
        kafkaTemplate.send(USER_VECTOR_TOPIC, objectMapper.writeValueAsString(userVector));
    }

    public void addRFIDToUser(UserRFID userRFID) throws JsonProcessingException {
        kafkaTemplate.send(USER_RFID_TOPIC, objectMapper.writeValueAsString(userRFID));
    }

}
