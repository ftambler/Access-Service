package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Application.DTOs.UserDTO;
import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserRFID;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Domain.Entities.VectorAndPicture;
import um.g7.Access_Service.Domain.Services.UserService;
import um.g7.Access_Service.Domain.Services.VectorizeService;


import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/users")
public class UserController {
    
    private UserService userService;
    private VectorizeService vectorizeService;

    public UserController(UserService userService, VectorizeService vectorizeService) {
        this.userService = userService;
        this.vectorizeService = vectorizeService;
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
    public ResponseEntity<String> insertVector(@PathVariable("id") UUID userId, @RequestBody String base64String) throws JsonProcessingException {
        VectorAndPicture response = vectorizeService.getVectorAndPicture(base64String);
        
        UserVector userVector = UserVector.builder().userId(userId).vector(response.vector()).build();
        userService.insertVector(userVector);
        
        return ResponseEntity.ok(response.base64String());
    }

    @PostMapping("/{id}/rfid/{rfid}")
    public ResponseEntity<UserRFID> insertRFID(@PathVariable("id") UUID userId, @PathVariable("rfid") String rfid) throws JsonProcessingException {
        UserRFID userRFID = new UserRFID(userId, rfid);
        return ResponseEntity.ok(userService.insertRFID(userRFID));

    }
    
}
