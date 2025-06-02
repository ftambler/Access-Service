package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import um.g7.Access_Service.Application.DTOs.DoorCreatorDTO;
import um.g7.Access_Service.Application.DTOs.DoorDTO;
import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Domain.Services.DoorService;

import java.util.List;

@RestController
@RequestMapping("/doors")
public class DoorController {

    private final DoorService doorService;

    public DoorController(DoorService doorService) {
        this.doorService = doorService;
    }

    @GetMapping()
    public ResponseEntity<List<DoorDTO>> listDoors() {
        List<DoorDTO> doorsDTO = doorService.fetchDoors().stream()
                .map(door -> new DoorDTO(door.getName(), door.getAccessLevel())).toList();
        return ResponseEntity.ok(doorsDTO);
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
