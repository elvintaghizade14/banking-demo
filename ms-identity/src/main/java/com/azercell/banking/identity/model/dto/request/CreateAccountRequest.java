package com.azercell.banking.identity.model.dto.request;

import com.azercell.banking.identity.model.enums.AccountStatus;
import com.azercell.banking.identity.model.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    private Long userId;
    private Currency currency;
    private BigDecimal balance;
    private AccountStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAccountRequest that = (CreateAccountRequest) o;
        return Objects.equals(userId, that.userId) && currency == that.currency
                && Objects.equals(balance, that.balance) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currency, balance, status);
    }
}
