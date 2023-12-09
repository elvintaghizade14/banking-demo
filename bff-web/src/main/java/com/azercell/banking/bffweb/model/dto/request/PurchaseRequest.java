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
public class PurchaseRequest {

    @NotBlank
    private String iban;

    @Min(0)
    private BigDecimal price;

    @NotNull
    private Currency currency;

    @NotBlank
    private String productName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseRequest that = (PurchaseRequest) o;
        return Objects.equals(iban, that.iban) && Objects.equals(price, that.price) && currency == that.currency
                && Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, price, currency, productName);
    }

}
