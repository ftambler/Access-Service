package um.g7.Access_Service.Application;

import io.grpc.*;
import io.grpc.ServerCall.Listener;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;
import um.g7.Access_Service.Domain.Services.JwtService;

@Component
@GrpcGlobalServerInterceptor
public class GrpcFilter implements ServerInterceptor {

    private final JwtService jwtService;

    public GrpcFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String raspberryToken = extractToken(headers); 
        if(!shouldNotFilter(call)) {

            if(raspberryToken == null || !jwtService.isValidDoorToken(raspberryToken)) {
                call.close(Status.UNAUTHENTICATED.withDescription("Invalid or missing token"), new Metadata());
                return new ServerCall.Listener<ReqT>() {};
            }

        }     
        
        return next.startCall(call, headers);
    }

    private <ReqT, RespT> boolean shouldNotFilter(ServerCall<ReqT, RespT> call) {
        String fullMethodName = call.getMethodDescriptor().getFullMethodName();
        
        return fullMethodName.equals("Access/Connect");
    }



    private String extractToken(Metadata headers) {
        String headerAuth = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));
        
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
    
}
