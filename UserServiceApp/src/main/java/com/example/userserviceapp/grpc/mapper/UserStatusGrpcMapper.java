package com.example.userserviceapp.grpc.mapper;

import com.example.jrpc.grpc.UserStatusGrpc;
import com.example.userserviceapp.entity.enums.UserStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserStatusGrpcMapper {
    UserStatusGrpc userStatusToUserStatusGrpc(UserStatus userStatus);
    @ValueMappings({
            @ValueMapping(source = "UNRECOGNIZED", target = "BLOCKED")
    })
    UserStatus userStatusGrpcToUserStatus(UserStatusGrpc userStatusGrpc);
}
