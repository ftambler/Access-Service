package um.g7.Access_Service.Domain.Services;

import org.springframework.stereotype.Service;

import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Domain.Exception.DoorAlreadyExists;
import um.g7.Access_Service.Infrastructure.KafkaProducers.DoorTopicProducer;
import um.g7.Access_Service.Infrastructure.Repositories.DoorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoorService {
    
    private final String secretKey = "asdfghjkl";

    private final DoorRepository doorRepository;
    private final DoorTopicProducer doorTopicProducer;

    public DoorService(DoorRepository doorRepository, DoorTopicProducer doorTopicProducer) {
        this.doorRepository = doorRepository;
        this.doorTopicProducer = doorTopicProducer;
    }

    public List<Door> fetchDoors() {
        return doorRepository.findAll();
    }

    public String createDoor(Door door) {
        Optional<Door> optionalDoor = doorRepository.findByName(door.getName());
        if (optionalDoor.isPresent())
            throw new DoorAlreadyExists("A door with that name already exists");

        door.setId(UUID.randomUUID());
        doorRepository.save(door);

        return door.getId().toString();
    }
}
