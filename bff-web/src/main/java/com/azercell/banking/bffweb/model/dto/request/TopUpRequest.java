package com.azercell.banking.bffweb.model.dto.request;

import com.azercell.banking.bffweb.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpRequest {

    @NotBlank
    private String iban;

    @NotNull
    private Currency currency;

    @Min(value = 1)
    private BigDecimal amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopUpRequest that = (TopUpRequest) o;
        return Objects.equals(iban, that.iban) && currency == that.currency && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, currency, amount);
    }

}
