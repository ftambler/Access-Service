package um.g7.Access_Service.Domain.Services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserRFID;
import um.g7.Access_Service.Domain.Entities.UserTableData;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Domain.Exception.UserNotFoundException;
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

    public void changeAccessLevel(UUID userId, int newLevel) throws JsonProcessingException, UserNotFoundException {
        Optional<UserEntity> optUser = userRepository.findById(userId);
        
        if(optUser.isEmpty()) throw new UserNotFoundException("User not found");
        UserEntity user = optUser.get();
        
        user.setAccessLevel(newLevel);
        userTopicProducer.addUser(user);
        userRepository.save(user);    
    }

    public List<UserTableData> getAllUsers() {
        return userRepository.findAllUsersWRfidFace().stream()
            .map(proj -> 
                UserTableData.builder()
                    .id(proj.getUserId())
                    .fullName(proj.getFullName())
                    .cid(proj.getCid())
                    .accessLevel(proj.getAccessLevel())
                    .hasRfid(proj.getRfid())
                    .hasFace(proj.getFace())
                    .build()).toList();
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
