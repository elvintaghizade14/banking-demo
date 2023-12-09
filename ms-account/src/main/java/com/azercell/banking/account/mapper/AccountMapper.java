package com.azercell.banking.account.mapper;

import com.azercell.banking.account.dao.entity.AccountEntity;
import com.azercell.banking.account.model.dto.AccountDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper extends EntityMapper<AccountDto, AccountEntity> {

}
