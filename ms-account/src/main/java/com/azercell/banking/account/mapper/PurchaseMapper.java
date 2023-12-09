package com.azercell.banking.account.mapper;

import com.azercell.banking.account.dao.entity.PurchaseEntity;
import com.azercell.banking.account.model.dto.PurchaseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PurchaseMapper extends EntityMapper<PurchaseDto, PurchaseEntity> {

}
