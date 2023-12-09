package com.azercell.banking.account.service;

import com.azercell.banking.account.dao.entity.AccountEntity;
import com.azercell.banking.account.dao.entity.PurchaseEntity;
import com.azercell.banking.account.dao.repository.AccountRepository;
import com.azercell.banking.account.dao.repository.PurchaseRepository;
import com.azercell.banking.account.exception.constant.ErrorMessage;
import com.azercell.banking.account.mapper.AccountMapper;
import com.azercell.banking.account.mapper.CustomMapper;
import com.azercell.banking.account.mapper.PurchaseMapper;
import com.azercell.banking.account.model.dto.*;
import com.azercell.banking.account.model.dto.request.CreateAccountRequest;
import com.azercell.banking.account.model.dto.request.PurchaseRequest;
import com.azercell.banking.account.model.dto.request.RefundRequest;
import com.azercell.banking.account.model.dto.request.TopUpRequest;
import com.azercell.banking.account.model.enums.Currency;
import com.azercell.banking.account.model.enums.PurchaseStatus;
import com.azercell.banking.commonlib.exception.AlreadyExistException;
import com.azercell.banking.commonlib.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.azercell.banking.account.exception.constant.ErrorMessage.ACCOUNT_NOT_FOUND_MESSAGE;
import static com.azercell.banking.account.model.enums.AccountStatus.ACTIVATED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountMapper accountMapper;
    private final PurchaseMapper purchaseMapper;
    private final AccountRepository accountRepository;
    private final PurchaseRepository purchaseRepository;

    @Transactional
    public AccountDto createAccount(CreateAccountRequest request) {
        final Long userId = request.getUserId();
        final Currency currency = request.getCurrency();
        boolean isAccountExists = accountRepository.existsByUserIdAndCurrencyAndStatus(userId, currency, request.getStatus());
        if (isAccountExists) {
            throw AlreadyExistException.of(ErrorMessage.ACCOUNT_ALREADY_EXIST_MESSAGE, userId, currency);
        }
        return accountMapper.toDto(accountRepository.save(CustomMapper.toAccountEntity(request)));

    }

    @Transactional
    public AccountDto topUp(Long userId, TopUpRequest request) {
        final String iban = request.getIban();
        final Currency currency = request.getCurrency();
        AccountEntity account = accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(userId, iban, currency, ACTIVATED)
                .orElseThrow(() -> DataNotFoundException.of(ACCOUNT_NOT_FOUND_MESSAGE, userId, iban, currency));
        account.setBalance(account.getBalance().add(request.getAmount()));
        return accountMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public PurchaseDto purchase(Long userId, PurchaseRequest request) {
        final String iban = request.getIban();
        final BigDecimal price = request.getPrice();
        final Currency currency = request.getCurrency();
        AccountEntity account = accountRepository.findByUserIdAndIbanAndCurrencyAndStatusAndBalanceGreaterThanEqual(
                        userId, iban, currency, ACTIVATED, price)
                .orElseThrow(() -> DataNotFoundException.of(ACCOUNT_NOT_FOUND_MESSAGE, userId, iban, currency));
        account.setBalance(account.getBalance().subtract(price));
        accountRepository.save(account);
        PurchaseEntity purchase = purchaseRepository.save(PurchaseEntity.builder()
                .userId(userId)
                .iban(iban)
                .price(price)
                .currency(Currency.AZN)
                .status(PurchaseStatus.APPROVED)
                .productName(request.getProductName())
                .build());
        return purchaseMapper.toDto(purchase);
    }

    @Transactional
    public void refund(Long userId, RefundRequest request) {
        final Long purchaseId = request.getPurchaseId();
        PurchaseEntity purchase = purchaseRepository.findByIdAndStatus(purchaseId, PurchaseStatus.APPROVED)
                .orElseThrow(() -> DataNotFoundException.of(ErrorMessage.PURCHASE_NOT_FOUND_MESSAGE, purchaseId));

        final String iban = purchase.getIban();
        final Currency currency = purchase.getCurrency();
        AccountEntity account = accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(userId, iban, currency, ACTIVATED)
                .orElseThrow(() -> DataNotFoundException.of(ACCOUNT_NOT_FOUND_MESSAGE, userId, iban, currency));
        account.setBalance(account.getBalance().add(purchase.getPrice()));
        accountRepository.save(account);

        purchase.setStatus(PurchaseStatus.REFUNDED);
        purchaseRepository.save(purchase);
    }

}
