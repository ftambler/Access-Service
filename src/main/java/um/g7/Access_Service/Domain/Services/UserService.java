package um.g7.Access_Service.Domain.Services;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.UserEntity;
import um.g7.Access_Service.Domain.Entities.UserRFID;
import um.g7.Access_Service.Domain.Entities.UserTableData;
import um.g7.Access_Service.Domain.Entities.UserVector;
import um.g7.Access_Service.Domain.Exception.UserAlreadyExists;
import um.g7.Access_Service.Domain.Exception.UserNotFoundException;
import um.g7.Access_Service.Infrastructure.KafkaProducers.DeletionTopicProducer;
import um.g7.Access_Service.Infrastructure.KafkaProducers.UserTopicProducer;
import um.g7.Access_Service.Infrastructure.Repositories.UserRFIDRepository;
import um.g7.Access_Service.Infrastructure.Repositories.UserRepository;
import um.g7.Access_Service.Infrastructure.Repositories.UserVectorRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserVectorRepository userVectorRepository;
    private final UserRFIDRepository userRFIDRepository;

    private final UserTopicProducer userTopicProducer;
    private final DeletionTopicProducer deletionTopicProducer;

    public UserService(UserRepository userRepository,
                       UserVectorRepository userVectorRepository,
                       UserTopicProducer userTopicProducer,
                       UserRFIDRepository userRFIDRepository,
                       DeletionTopicProducer deletionTopicProducer) {
        this.userRepository = userRepository;
        this.userVectorRepository = userVectorRepository;
        this.userTopicProducer = userTopicProducer;
        this.userRFIDRepository = userRFIDRepository;
        this.deletionTopicProducer = deletionTopicProducer;
    }

    public UserEntity createUser(UserEntity user) throws JsonProcessingException {
        Optional<UserEntity> optionalUser = userRepository.findByCid(user.getCid());

        if (optionalUser.isPresent())
            throw new UserAlreadyExists("Another user has the same document");

        userTopicProducer.addUser(user);
        return userRepository.save(user);
    }

    public UserVector insertVector(UserVector userVector) throws JsonProcessingException {
        Optional<UserVector> optionalUserVector = userVectorRepository.findById(userVector.getUserId());

        if (optionalUserVector.isPresent())
            userVector = optionalUserVector.get();

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

    public Page<UserTableData> getPaginatedUsers(int page, int pageSize, String nameLookUp) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("full_name"));

        return userRepository.findAllUsersWRfidFacePageable(nameLookUp, pageable).map(proj ->
                        UserTableData.builder()
                                .id(proj.getUserId())
                                .fullName(proj.getFullName())
                                .cid(proj.getCid())
                                .accessLevel(proj.getAccessLevel())
                                .hasRfid(proj.getRfid())
                                .hasFace(proj.getFace())
                                .build());
    }

    public void deleteUser(UUID userId) throws UserNotFoundException, JsonProcessingException {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("Cannot delete non existant user");

        Optional<UserRFID> optionalUserRFID = userRFIDRepository.findById(userId);

        if (optionalUserRFID.isPresent())
            userRFIDRepository.deleteById(userId);

        Optional<UserVector> optionalUserVector = userVectorRepository.findById(userId);

        if (optionalUserVector.isPresent())
            userVectorRepository.deleteById(userId);

        deletionTopicProducer.deleteUser(optionalUser.get());
        userRepository.deleteById(userId);
    }
}
