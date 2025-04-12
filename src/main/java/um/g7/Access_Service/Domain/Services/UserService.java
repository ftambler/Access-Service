package um.g7.Access_Service.Domain.Services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Application.DTOs.UserDTO;
import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Infrastructure.UserRepository;
import um.g7.Access_Service.Infrastructure.UserTopicProducer;

@Service
public class UserService {
    
    private UserRepository userRepository;
    private UserTopicProducer userTopicProducer;

    
    public UserService(UserRepository userRepository, UserTopicProducer userTopicProducer) {
        this.userRepository = userRepository;
        this.userTopicProducer = userTopicProducer;
    }

    public UserEntity createUser(UserDTO userDTO) throws JsonProcessingException {
        UserEntity user = UserEntity.builder()
            .userId(UUID.randomUUID())
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .cid(userDTO.getCid()).build();

        userRepository.save(user);
        userTopicProducer.addUser(user);
        
        return user;
    }

}
