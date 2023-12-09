package com.azercell.banking.account.mapper;

import com.azercell.banking.account.dao.entity.AccountEntity;
import com.azercell.banking.account.model.dto.request.CreateAccountRequest;

import java.util.concurrent.atomic.AtomicLong;

public final class CustomMapper {

    private static final AtomicLong ibanCounter = new AtomicLong(10000000000000000L);

    public static AccountEntity toAccountEntity(CreateAccountRequest request) {
        return AccountEntity.builder()
                .iban(generateIban())
                .currency(request.getCurrency())
                .balance(request.getBalance())
                .status(request.getStatus())
                .userId(request.getUserId())
                .build();
    }

    private static String generateIban() {
        return "AZ21NABZ" + ibanCounter.incrementAndGet() + "944";
    }

}
