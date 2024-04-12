package com.example.userserviceapp.grpc;

import com.example.jrpc.grpc.PhoneRequest;
import com.example.jrpc.grpc.UserInfoResponse;
import com.example.jrpc.grpc.UserStatusGrpc;
import com.example.jrpc.grpc.UserStatusServiceGrpc;
import com.example.userserviceapp.entity.User;
import com.example.userserviceapp.grpc.mapper.UserStatusGrpcMapper;
import com.example.userserviceapp.mapper.UserMapper;
import com.example.userserviceapp.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class UserStatusServiceGrpcImpl extends UserStatusServiceGrpc.UserStatusServiceImplBase {
    private final UserRepository userRepository;
    private final UserStatusGrpcMapper userStatusGrpcMapper;
    private final Logger LOGGER = LoggerFactory.getLogger("gRPC producer");
    @Override
    public void getUserInfoByPhone(PhoneRequest request, StreamObserver<UserInfoResponse> responseObserver) {
        Optional<User> userOptional = userRepository.findByPhone(request.getPhone());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            UserInfoResponse userInfoResponse = UserInfoResponse.newBuilder()
                    .setUserId(user.getId().toString())
                    .setUserStatusGrpc(userStatusGrpcMapper.userStatusToUserStatusGrpc(user.getUserStatus()))
                    .setExpirationDate(user.getExpirationDate().toString())
                    .build();
            responseObserver.onNext(userInfoResponse);
            responseObserver.onCompleted();
            LOGGER.info("request {"+request.getPhone()+"}, response {"+user.getId()+"}");
        }
        else {
            responseObserver.onNext(UserInfoResponse.newBuilder().build());
            responseObserver.onCompleted();
            LOGGER.warn("request {"+request.getPhone()+"}, user not found");
        }
    }
}
