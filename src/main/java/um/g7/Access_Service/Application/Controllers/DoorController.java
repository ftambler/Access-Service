package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import um.g7.Access_Service.Application.DTOs.DoorCreatorDTO;
import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Domain.Services.DoorService;

@RestController
@RequestMapping("/doors")
public class DoorController {

    private final DoorService doorService;

    public DoorController(DoorService doorService) {
        this.doorService = doorService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDoor(@RequestBody DoorCreatorDTO doorCreatorDTO) {
        Door door = Door.builder()
                .name(doorCreatorDTO.getDoorName())
                .accessLevel(doorCreatorDTO.getAccessLevel())
                .passcode(doorCreatorDTO.getPasscode()).build();
        return ResponseEntity.ok(doorService.createDoor(door));
    }
}
