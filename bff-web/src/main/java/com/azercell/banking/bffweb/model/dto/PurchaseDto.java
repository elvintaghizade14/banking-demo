package com.azercell.banking.bffweb.model.dto;

import com.azercell.banking.bffweb.model.enums.Currency;
import com.azercell.banking.bffweb.model.enums.PurchaseStatus;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseDto that = (PurchaseDto) o;
        return id == that.id && userId == that.userId && Objects.equals(iban, that.iban)
                && Objects.equals(price, that.price) && currency == that.currency
                && Objects.equals(productName, that.productName) && status == that.status
                && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, iban, price, currency, productName, status, createdAt, updatedAt);
    }

}
