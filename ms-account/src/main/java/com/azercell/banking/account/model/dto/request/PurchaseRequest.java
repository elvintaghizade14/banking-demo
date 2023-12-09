package com.azercell.banking.account.model.dto.request;

import com.azercell.banking.account.model.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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

}
