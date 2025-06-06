package um.g7.Access_Service.Application.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import um.g7.Access_Service.Application.DTOs.UserDTO;
import um.g7.Access_Service.Domain.Entities.*;
import um.g7.Access_Service.Domain.Exception.UserNotFoundException;
import um.g7.Access_Service.Domain.Services.UserService;
import um.g7.Access_Service.Domain.Services.VectorizeService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@RestController
@RequestMapping("/users")
public class UserController {
    
    private UserService userService;
    private VectorizeService vectorizeService;

    public UserController(UserService userService, VectorizeService vectorizeService) {
        this.userService = userService;
        this.vectorizeService = vectorizeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> allUsers = userService.getAllUsers().stream()
            .map(user -> new UserDTO(user.getId(), user.getFullName(), user.getCid(), user.getAccessLevel(), user.isHasRfid(), user.isHasFace()))
            .toList();

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getPaginatedUsers(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        List<UserTableData> paginatedUsers = userService.getPaginatedUsers(page, pageSize);
        List<UserDTO> userDTOList = paginatedUsers.stream().map(user -> UserDTO.builder()
                .uuid(user.getId())
                .fullName(user.getFullName())
                .cid(user.getCid())
                .hasRfid(user.isHasRfid())
                .hasFace(user.isHasFace()).build()).toList();

        return ResponseEntity.ok(userDTOList);
    }

    @PostMapping()
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDTO userDTO) throws JsonProcessingException {
        UserEntity user = UserEntity.builder()
                .userId(UUID.randomUUID())
                .fullName(userDTO.getFullName())
                .cid(userDTO.getCid())
                .accessLevel(userDTO.getAccessLevel()).build();

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

    @PutMapping("/{id}/change-access-level/{newLevel}")
    public ResponseEntity<ResponseStatus> changeAccessLevel(@PathVariable("id") UUID userId, @PathVariable("newLevel") int newLevel) throws JsonProcessingException, UserNotFoundException {
        userService.changeAccessLevel(userId, newLevel);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStatus> deleteUser(@PathVariable("id") UUID userId) throws UserNotFoundException, JsonProcessingException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
    
}
