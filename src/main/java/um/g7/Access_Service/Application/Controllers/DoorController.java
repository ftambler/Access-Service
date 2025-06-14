package um.g7.Access_Service.Application.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import um.g7.Access_Service.Application.DTOs.DoorCreatorDTO;
import um.g7.Access_Service.Application.DTOs.DoorDTO;
import um.g7.Access_Service.Domain.Entities.Door;
import um.g7.Access_Service.Domain.Services.DoorService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doors")
public class DoorController {

    private final DoorService doorService;

    public DoorController(DoorService doorService) {
        this.doorService = doorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoorDTO>> listDoors() {
        List<DoorDTO> doorsDTO = doorService.fetchDoors().stream()
                .map(door -> new DoorDTO(door.getId(), door.getName(), door.getAccessLevel())).toList();
        return ResponseEntity.ok(doorsDTO);
    }

    @GetMapping
    public ResponseEntity<Page<DoorDTO>> paginatedDoors(@RequestParam(name = "page") int page, @RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "doorNameLookUp", defaultValue = "") String doorNameLookUp) {
        Page<DoorDTO> paginatedDoors = doorService.paginatedDoors(page, pageSize, doorNameLookUp).map(door ->
                DoorDTO.builder()
                        .id(door.getId())
                        .accessLevel(door.getAccessLevel())
                        .name(door.getName()).build());

        return ResponseEntity.ok(paginatedDoors);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createDoor(@RequestBody DoorCreatorDTO doorCreatorDTO) throws JsonProcessingException {
        Door door = Door.builder()
                .name(doorCreatorDTO.getDoorName())
                .accessLevel(doorCreatorDTO.getAccessLevel())
                .passcode(doorCreatorDTO.getPasscode()).build();

        return new ResponseEntity<String>(doorService.createDoor(door), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/change-access-level/{level}")
    public ResponseEntity<String> modifyDoorAccessLevel(@PathVariable("id") UUID doorId, @PathVariable("level") int level) throws JsonProcessingException {
        doorService.changeDoorAccessLevel(doorId, level);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/change-password/{passcode}")
    public ResponseEntity<String> modifyDoorPasscode(@PathVariable("id") UUID doorId, @PathVariable("passcode") String passcode) throws JsonProcessingException {
        doorService.changeDoorPasscode(doorId, passcode);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoor(@PathVariable("id") UUID doorId) throws JsonProcessingException {
        doorService.deleteDoor(doorId);
        return ResponseEntity.ok().build();
    }
}
