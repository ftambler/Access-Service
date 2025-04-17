package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Application.DTOs.UserDTO;
import um.g7.Access_Service.Application.DTOs.VectorDTO;
import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Domain.Services.UserService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;


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
    public ResponseEntity<UserVector> insertVector(@PathVariable UUID userId, @RequestBody VectorDTO vectorDTO) throws JsonProcessingException {

        UserVector userVector = UserVector.builder().userId(userId).vector(vectorDTO.vector()).build();
        return ResponseEntity.ok(userService.insertVector(userVector));
    }
}
