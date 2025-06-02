package um.g7.Access_Service.Infrastructure.KafkaProducers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import um.g7.Access_Service.Application.DTOs.DeletionRequest;
import um.g7.Access_Service.Application.DTOs.DeletionType;
import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Domain.Entities.UserEntity;

@Component
public class DeletionTopicProducer {
    private final String DELETION_TOPIC = "deletion";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DeletionTopicProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void deleteUser(UserEntity user) throws JsonProcessingException {
        DeletionRequest deletionRequest = DeletionRequest.builder()
                .id(user.getUserId())
                .type(DeletionType.USER).build();
        kafkaTemplate.send(DELETION_TOPIC, objectMapper.writeValueAsString(deletionRequest));
    }

    public void deleteDoor(Door door) throws JsonProcessingException {
        DeletionRequest deletionRequest = DeletionRequest.builder()
                .id(door.getId())
                .type(DeletionType.DOOR).build();
        kafkaTemplate.send(DELETION_TOPIC, objectMapper.writeValueAsString(deletionRequest));
    }

}
