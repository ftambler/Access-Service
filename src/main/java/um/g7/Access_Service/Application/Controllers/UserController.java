package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Application.DTOs.UserDTO;
import um.g7.Access_Service.Application.DTOs.VectorDTO;
import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserRFID;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Domain.Services.UserService;

import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/users")
public class UserController {
    
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDTO userDTO) throws JsonProcessingException {
        UserEntity user = UserEntity.builder()
                .userId(UUID.randomUUID())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .cid(userDTO.getCid()).build();

        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/{id}/vector")
    public ResponseEntity<UserVector> insertVector(@PathVariable("id") UUID userId, @RequestBody VectorDTO vectorDTO) throws JsonProcessingException {
        UserVector userVector = UserVector.builder().userId(userId).vector(vectorDTO.vector()).build();
        return ResponseEntity.ok(userService.insertVector(userVector));
    }

    @PostMapping("/{id}/rfid/{rfid}")
    public ResponseEntity<UserRFID> insertRFID(@PathVariable("id") UUID userId, @PathVariable("rfid") long rfid) throws JsonProcessingException {
        UserRFID userRFID = UserRFID.builder().userId(userId).rfid(rfid).build();
        return ResponseEntity.ok(userService.insertRFID(userRFID));

    }
    
}
