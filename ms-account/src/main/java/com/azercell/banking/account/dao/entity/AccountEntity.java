package com.azercell.banking.account.dao.entity;


import com.azercell.banking.account.model.enums.AccountStatus;
import com.azercell.banking.account.model.enums.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "accounts")
@ToString(callSuper = true)
public class AccountEntity extends BaseEntity {

    private static final long serialVersionUID = 3470424543096822461L;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @NotBlank
    @Column(name = "iban")
    private String iban;

    @NotNull
    @Column(name = "currency")
    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    @NotNull
    @Column(name = "balance")
    private BigDecimal balance;

    @NotNull
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

}
