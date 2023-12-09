package com.azercell.banking.bffweb.model.dto;

import com.azercell.banking.bffweb.model.enums.AccountStatus;
import com.azercell.banking.bffweb.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private long id;
    private long userId;
    private String iban;
    private Currency currency;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return id == that.id && userId == that.userId && Objects.equals(iban, that.iban) && currency == that.currency
                && Objects.equals(balance, that.balance) && status == that.status
                && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, iban, currency, balance, status, createdAt, updatedAt);
    }

}
