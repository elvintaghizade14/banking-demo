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
public class TopUpRequest {

    @NotBlank
    private String iban;

    @NotNull
    private Currency currency;

    @Min(value = 1)
    private BigDecimal amount;

}
