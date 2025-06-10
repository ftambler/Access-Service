package um.g7.Access_Service.Domain.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public String createDoor(Door door) throws JsonProcessingException {
        Optional<Door> optionalDoor = doorRepository.findByName(door.getName());
        if (optionalDoor.isPresent())
            throw new DoorAlreadyExists("A door with that name already exists");

        door.setId(UUID.randomUUID());
        door.setPasscode(passwordEncoder.encode(door.getPasscode()));

        doorTopicProducer.addDoor(door);
        doorRepository.save(door);

        return door.getId().toString();
    }
  
    public void changeDoorAccessLevel(UUID doorId, int level) throws JsonProcessingException {
        Optional<Door> optionalDoor = doorRepository.findById(doorId);

        if (optionalDoor.isEmpty())
            throw new DoorNotFoundException("Could not found the door");

        Door door = optionalDoor.get();
        door.setAccessLevel(level);

        doorTopicProducer.addDoor(door);
        doorRepository.save(door);
    }

    public void changeDoorPasscode(UUID doorId, String passcode) throws JsonProcessingException {
        Optional<Door> optionalDoor = doorRepository.findById(doorId);

        if (optionalDoor.isEmpty())
            throw new DoorNotFoundException("Could not found the door");

        Door door = optionalDoor.get();
        door.setPasscode(passcode);

        doorTopicProducer.addDoor(door);
        doorRepository.save(door);
    }
  
    public void deleteDoor(UUID doorId) throws JsonProcessingException {
        Optional<Door> optionalDoor = doorRepository.findById(doorId);

        if (optionalDoor.isEmpty())
            throw new DoorNotFoundException("Cannot find door to delete");

        deletionTopicProducer.deleteDoor(optionalDoor.get());
        doorRepository.deleteById(doorId);
    }

    public Page<Door> paginatedDoors(int page, int pageSize, String doorNameLookUp) {
        Pageable pageable = PageRequest.of(page, pageSize);

        return doorRepository.findAllByDoorNameLookUp(doorNameLookUp, pageable);
    }
}
