package um.g7.Access_Service.Infrastructure.KafkaProducers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import um.g7.Access_Service.Domain.Entities.Door;

@Component
public class DoorTopicProducer {
    

    private final String DOOR_TOPIC = "door";
    
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DoorTopicProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void addDoor(Door door) throws JsonProcessingException {
        kafkaTemplate.send(DOOR_TOPIC, objectMapper.writeValueAsString(door));
    }

}
