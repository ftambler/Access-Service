package um.g7.Access_Service.Domain.Services;

import org.springframework.stereotype.Service;

import um.g7.Access_Service.Domain.Entities.Door;
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

    public boolean validAndCreateIfNull(String doorName, String passcode, int doorAccessLevel) {
        if (!secretKey.equals(passcode))
            return false;

        Optional<Door> doorOptional = doorRepository.findByName(doorName);

        if (doorOptional.isEmpty()) {
            Door door = new Door(UUID.randomUUID(), doorName, doorAccessLevel);
            doorRepository.save(door);
            doorTopicProducer.addDoor(door);
            return true;
        }

        Door door = doorOptional.get();
        door.setAccessLevel(doorAccessLevel);
        doorRepository.save(door);
        doorTopicProducer.addDoor(door);

        return true;
    }

    public List<Door> fetchDoors() {
        return doorRepository.findAll();
    }

}
