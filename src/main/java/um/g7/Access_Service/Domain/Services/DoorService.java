package um.g7.Access_Service.Domain.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Domain.Exception.DoorAlreadyExists;
import um.g7.Access_Service.Domain.Exception.DoorNotFoundException;
import um.g7.Access_Service.Infrastructure.KafkaProducers.DeletionTopicProducer;
import um.g7.Access_Service.Infrastructure.KafkaProducers.DoorTopicProducer;
import um.g7.Access_Service.Infrastructure.Repositories.DoorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoorService {

    private final DoorRepository doorRepository;
    private final DoorTopicProducer doorTopicProducer;
    private final DeletionTopicProducer deletionTopicProducer;

    private final PasswordEncoder passwordEncoder;

    public DoorService(DoorRepository doorRepository, DoorTopicProducer doorTopicProducer, DeletionTopicProducer deletionTopicProducer) {
        this.doorRepository = doorRepository;
        this.doorTopicProducer = doorTopicProducer;
        this.deletionTopicProducer = deletionTopicProducer;

        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Door> fetchDoors() {
        return doorRepository.findAll();
    }

    public String createDoor(Door door) {
        Optional<Door> optionalDoor = doorRepository.findByName(door.getName());
        if (optionalDoor.isPresent())
            throw new DoorAlreadyExists("A door with that name already exists");

        door.setId(UUID.randomUUID());
        door.setPasscode(passwordEncoder.encode(door.getPasscode()));

        doorTopicProducer.addDoor(door);
        doorRepository.save(door);

        return door.getId().toString();
    }

    public void deleteDoor(UUID doorId) throws JsonProcessingException {
        Optional<Door> optionalDoor = doorRepository.findById(doorId);

        if (optionalDoor.isEmpty())
            throw new DoorNotFoundException("Cannot find door to delete");

        deletionTopicProducer.deleteDoor(optionalDoor.get());
        doorRepository.deleteById(doorId);
    }
}
