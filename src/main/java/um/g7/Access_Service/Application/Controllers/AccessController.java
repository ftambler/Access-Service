package um.g7.Access_Service.Application.Controllers;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.grpc.sample.proto.AccessGrpc;
import org.springframework.grpc.sample.proto.AccessProto.FailedAccessDTO;
import org.springframework.grpc.sample.proto.AccessProto.MailFailDTO;
import org.springframework.grpc.sample.proto.AccessProto.SubmitResponseDTO;
import org.springframework.grpc.sample.proto.AccessProto.SuccessfulAccessDTO;
import um.g7.Access_Service.Domain.Entities.AccessTypeEnum;
import um.g7.Access_Service.Domain.Entities.FailedAccess;
import um.g7.Access_Service.Domain.Entities.SuccessfulAccess;
import um.g7.Access_Service.Domain.Services.AccessService;
import um.g7.Access_Service.Domain.Services.EmailService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@GrpcService
public class AccessController extends AccessGrpc.AccessImplBase{

    private final AccessService accessService;
    private final EmailService emailService;

    public AccessController(AccessService accessService, EmailService emailService) {
        this.accessService = accessService;
        this.emailService = emailService;
    }

    
    @Override
    public void sendEmail(MailFailDTO request, StreamObserver<SubmitResponseDTO> responseObserver) {
        emailService.sendEmail(request.getDoorName(), request.getTime(), AccessTypeEnum.values()[request.getAccessTypeValue()].name(), request.getFailedStreak());
        
        responseObserver.onNext(SubmitResponseDTO.newBuilder().setResponse("Email sent!").build());
        responseObserver.onCompleted();
    }

    @Override
    public void submitFailedAccess(FailedAccessDTO request, StreamObserver<SubmitResponseDTO> responseObserver) {
        FailedAccess failedAccess = FailedAccess.builder()
            .accessId(UUID.randomUUID())
            .accessDate(LocalDateTime.ofEpochSecond(request.getTime(), 0, ZoneOffset.of("-03:00")))
            .accessType(AccessTypeEnum.values()[request.getAccessTypeValue()].name())
            .doorName(request.getDoorName())
            .build();

        SubmitResponseDTO response = accessService.submitFailedAccess(failedAccess, responseObserver);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void submitSuccessfulAccess(SuccessfulAccessDTO request, StreamObserver<SubmitResponseDTO> responseObserver) {
        SuccessfulAccess successfulAccess = SuccessfulAccess.builder()
            .accessId(UUID.randomUUID())
            .accessDate(LocalDateTime.ofEpochSecond(request.getTime(), 0, ZoneOffset.of("-03:00")))
            .fullName(request.getFullName())
            .cid(request.getCid())
            .accessType(AccessTypeEnum.values()[request.getAccessTypeValue()].name())
            .doorName(request.getDoorName())
            .build();
        
        SubmitResponseDTO response = accessService.submitSuccessfulAccess(successfulAccess, responseObserver);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
