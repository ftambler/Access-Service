package um.g7.Access_Service.Domain.Services;

import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Infrastructure.Repositories.DoorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoorService {
    
    private final String secretKey = "agnip";

    private final DoorRepository doorRepository;

    public DoorService(DoorRepository doorRepository) {
        this.doorRepository = doorRepository;
    }

    public boolean validAndCreateIfNull(String doorName, String passcode, int doorAccessLevel) {
        if (!secretKey.equals(passcode))
            return false;

        Optional<Door> doorOptional = doorRepository.findByName(doorName);

        if (doorOptional.isEmpty()) {
            doorRepository.save(new Door(UUID.randomUUID(), doorName, doorAccessLevel));
            return true;
        }

        Door door = doorOptional.get();
        door.setAccessLevel(doorAccessLevel);
        doorRepository.save(door);

        return true;
    }

    public List<Door> fetchDoors() {
        return doorRepository.findAll();
    }

}
