package um.g7.Access_Service.Domain;

import org.springframework.stereotype.Service;

import io.grpc.stub.StreamObserver;
import um.g7.Access_Service.Infrastructure.AccessRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.grpc.sample.proto.AccessGrpc;
import org.springframework.grpc.sample.proto.AccessProto.FailedAccess;
import org.springframework.grpc.sample.proto.AccessProto.SubmitResponse;
import org.springframework.grpc.sample.proto.AccessProto.SuccessfulAccess;

@Service
public class AccessService extends AccessGrpc.AccessImplBase{

    private final AccessRepository accessRepository;

    public AccessService(AccessRepository accessRepository) {
        this.accessRepository = accessRepository;
    }

    @Override
    public void sumbitFailedAccess(FailedAccess request, StreamObserver<SubmitResponse> responseObserver) {

        super.sumbitFailedAccess(request, responseObserver);
    }

    @Override
    public void sumbitSuccessfulAccess(SuccessfulAccess request, StreamObserver<SubmitResponse> responseObserver) {
        Access temp = Access.builder()
            .accessId(UUID.randomUUID())
            .accessDate(LocalDateTime.ofEpochSecond(request.getTime(), 0, ZoneOffset.of("-03:00")))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .cid(request.getCid())
            .build();

        accessRepository.save(temp);

        SubmitResponse response = SubmitResponse.newBuilder().setResponse(temp.toString()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
}