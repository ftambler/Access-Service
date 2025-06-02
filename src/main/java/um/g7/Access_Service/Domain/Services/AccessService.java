package um.g7.Access_Service.Domain.Services;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.sample.proto.AccessProto.SubmitResponseDTO;
import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.FailedAccess;
import um.g7.Access_Service.Domain.Entities.SuccessfulAccess;
import um.g7.Access_Service.Infrastructure.Repositories.FailedAccessRepository;
import um.g7.Access_Service.Infrastructure.Repositories.SuccessfulAccessRepository;

@Service
public class AccessService {

    private final SuccessfulAccessRepository successfulAccessRepository;
    private final FailedAccessRepository failedAccessRepository;

    public AccessService(SuccessfulAccessRepository sARepository, FailedAccessRepository fARepository) {
        this.successfulAccessRepository = sARepository;
        this.failedAccessRepository = fARepository;
    }

    public SubmitResponseDTO submitFailedAccess(FailedAccess failedAccess, StreamObserver<SubmitResponseDTO> responseObserver) {
        failedAccessRepository.save(failedAccess);

        return SubmitResponseDTO.newBuilder().setResponse(failedAccess.toString()).build(); 
    }

    public SubmitResponseDTO submitSuccessfulAccess(SuccessfulAccess successfulAccess, StreamObserver<SubmitResponseDTO> responseObserver) {
        successfulAccessRepository.save(successfulAccess);

        return SubmitResponseDTO.newBuilder().setResponse(successfulAccess.toString()).build();
    }
    
}