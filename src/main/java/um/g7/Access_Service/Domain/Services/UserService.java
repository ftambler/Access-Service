package um.g7.Access_Service.Domain.Services;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserRFID;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Infrastructure.KafkaProducers.UserTopicProducer;
import um.g7.Access_Service.Infrastructure.Repositories.UserRFIDRepository;
import um.g7.Access_Service.Infrastructure.Repositories.UserRepository;
import um.g7.Access_Service.Infrastructure.Repositories.UserVectorRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserVectorRepository userVectorRepository;
    private final UserRFIDRepository userRFIDRepository;

    private final UserTopicProducer userTopicProducer;

    public UserService(UserRepository userRepository,
                       UserVectorRepository userVectorRepository,
                       UserTopicProducer userTopicProducer,
                       UserRFIDRepository userRFIDRepository) {
        this.userRepository = userRepository;
        this.userVectorRepository = userVectorRepository;
        this.userTopicProducer = userTopicProducer;
        this.userRFIDRepository = userRFIDRepository;
    }

    public UserEntity createUser(UserEntity user) throws JsonProcessingException {
        userTopicProducer.addUser(user);
        return userRepository.save(user);
    }

    public UserVector insertVector(UserVector userVector) throws JsonProcessingException {
        userTopicProducer.addVectorToUser(userVector);
        return userVectorRepository.save(userVector);
    }

    public UserRFID insertRFID(UserRFID userRFID) throws JsonProcessingException {
        userTopicProducer.addRFIDToUser(userRFID);
        return userRFIDRepository.save(userRFID);
    }
}
