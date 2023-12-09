package com.azercell.banking.account.dao.repository;

import com.azercell.banking.account.dao.entity.AccountEntity;
import com.azercell.banking.account.model.enums.AccountStatus;
import com.azercell.banking.account.model.enums.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    boolean existsByUserIdAndCurrencyAndStatus(Long userId, Currency currency, AccountStatus status);

    Optional<AccountEntity> findByUserIdAndIbanAndCurrencyAndStatus(Long userId, String iban,
                                                                    Currency currency, AccountStatus status);

    Optional<AccountEntity> findByUserIdAndIbanAndCurrencyAndStatusAndBalanceGreaterThanEqual(
            Long userId, String iban, Currency currency, AccountStatus status, BigDecimal balance);

}
