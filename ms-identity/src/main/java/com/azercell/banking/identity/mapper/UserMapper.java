package com.azercell.banking.identity.mapper;

import com.azercell.banking.identity.dao.entity.UserEntity;
import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.dto.request.CreateUserRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = PasswordEncoderMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<UserDto, UserEntity> {

    @PasswordMappings
    @Mapping(target = "status", constant = "ACTIVE")
    UserEntity toEntity(CreateUserRequest request);

}
