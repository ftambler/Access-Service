package um.g7.Access_Service.Domain.Services;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import um.g7.Access_Service.Domain.Entities.FailedAccess;
import um.g7.Access_Service.Domain.Entities.SuccessfulAccess;
import um.g7.Access_Service.Infrastructure.Repositories.FailedAccessRepository;
import um.g7.Access_Service.Infrastructure.Repositories.SuccessfulAccessRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.grpc.sample.proto.AccessGrpc;
import org.springframework.grpc.sample.proto.AccessProto.FailedAccessDTO;
import org.springframework.grpc.sample.proto.AccessProto.SubmitResponseDTO;
import org.springframework.grpc.sample.proto.AccessProto.SuccessfulAccessDTO;

@GrpcService
public class AccessService extends AccessGrpc.AccessImplBase{

    private final SuccessfulAccessRepository successfulAccessRepository;
    private final FailedAccessRepository failedAccessRepository;

    public AccessService(SuccessfulAccessRepository sARepository, FailedAccessRepository fARepository) {
        this.successfulAccessRepository = sARepository;
        this.failedAccessRepository = fARepository;
    }

    @Override
    public void submitFailedAccess(FailedAccessDTO request, StreamObserver<SubmitResponseDTO> responseObserver) {
        FailedAccess temp = FailedAccess.builder()
            .accessId(UUID.randomUUID())
            .accessDate(LocalDateTime.ofEpochSecond(request.getTime(), 0, ZoneOffset.of("-03:00")))
            .build();

        failedAccessRepository.save(temp);

        SubmitResponseDTO response = SubmitResponseDTO.newBuilder().setResponse(temp.toString()).build(); 
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitSuccessfulAccess(SuccessfulAccessDTO request, StreamObserver<SubmitResponseDTO> responseObserver) {
        SuccessfulAccess temp = SuccessfulAccess.builder()
            .accessId(UUID.randomUUID())
            .accessDate(LocalDateTime.ofEpochSecond(request.getTime(), 0, ZoneOffset.of("-03:00")))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .cid(request.getCid())
            .build();

        successfulAccessRepository.save(temp);

        SubmitResponseDTO response = SubmitResponseDTO.newBuilder().setResponse(temp.toString()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
}