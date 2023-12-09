package com.azercell.banking.account.model.dto;

import com.azercell.banking.account.model.enums.Currency;
import com.azercell.banking.account.model.enums.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {

    private long id;
    private long userId;
    private String iban;
    private BigDecimal price;
    private Currency currency;
    private String productName;
    private PurchaseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
