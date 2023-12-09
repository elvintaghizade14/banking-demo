package com.azercell.banking.identity.mapper;

import org.mapstruct.Mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "password", qualifiedBy = EncodedMapping.class)
public @interface PasswordMappings {
}
