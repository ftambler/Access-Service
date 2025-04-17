package um.g7.Access_Service.Domain.Services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Application.DTOs.UserDTO;
import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Infrastructure.KafkaProducers.UserTopicProducer;
import um.g7.Access_Service.Infrastructure.Repositories.UserRepository;
import um.g7.Access_Service.Infrastructure.Repositories.UserVectorRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserVectorRepository userVectorRepository;

    private final UserTopicProducer userTopicProducer;

    public UserService(UserRepository userRepository,
                       UserVectorRepository userVectorRepository,
                       UserTopicProducer userTopicProducer) {
        this.userRepository = userRepository;
        this.userVectorRepository = userVectorRepository;
        this.userTopicProducer = userTopicProducer;
    }

    public UserEntity createUser(UserEntity user) throws JsonProcessingException {

            userTopicProducer.addUser(user);
            return userRepository.save(user);
    }

    public UserVector insertVector(UserVector userVector) throws JsonProcessingException {

        userTopicProducer.addVectorToUser(userVector);
        return userVectorRepository.save(userVector);
    }
}
