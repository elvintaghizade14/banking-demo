package com.azercell.banking.account.service;

import com.azercell.banking.account.dao.entity.AccountEntity;
import com.azercell.banking.account.dao.entity.PurchaseEntity;
import com.azercell.banking.account.dao.repository.AccountRepository;
import com.azercell.banking.account.dao.repository.PurchaseRepository;
import com.azercell.banking.account.mapper.AccountMapper;
import com.azercell.banking.account.mapper.PurchaseMapper;
import com.azercell.banking.account.model.dto.AccountDto;
import com.azercell.banking.account.model.dto.PurchaseDto;
import com.azercell.banking.account.model.dto.request.CreateAccountRequest;
import com.azercell.banking.account.model.dto.request.PurchaseRequest;
import com.azercell.banking.account.model.dto.request.RefundRequest;
import com.azercell.banking.account.model.dto.request.TopUpRequest;
import com.azercell.banking.account.model.enums.AccountStatus;
import com.azercell.banking.account.model.enums.Currency;
import com.azercell.banking.account.model.enums.PurchaseStatus;
import com.azercell.banking.commonlib.exception.AlreadyExistException;
import com.azercell.banking.commonlib.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AccountService.class})
@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @MockBean
    private PurchaseMapper purchaseMapper;

    @MockBean
    private PurchaseRepository purchaseRepository;

    /**
     * Method under test: {@link AccountService#createAccount(CreateAccountRequest)}
     */
    @Test
    void testCreateAccount() {
        when(accountRepository.existsByUserIdAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any())).thenThrow(new DataNotFoundException("An error occurred"));
        assertThrows(DataNotFoundException.class, () -> accountService.createAccount(new CreateAccountRequest()));
        verify(accountRepository).existsByUserIdAndCurrencyAndStatus(Mockito.<Long>any(), any(), any());
    }

    /**
     * Method under test: {@link AccountService#createAccount(CreateAccountRequest)}
     */
    @Test
    void testCreateAccount2() {
        AccountDto.AccountDtoBuilder builderResult = AccountDto.builder();
        AccountDto.AccountDtoBuilder balanceResult = builderResult.balance(new BigDecimal("2.3"));
        AccountDto.AccountDtoBuilder statusResult = balanceResult.createdAt(LocalDate.of(1970, 1, 1).atStartOfDay())
                .currency(Currency.AZN)
                .iban("Iban")
                .id(1L)
                .status(AccountStatus.ACTIVATED);
        AccountDto buildResult = statusResult.updatedAt(LocalDate.of(1970, 1, 1).atStartOfDay()).userId(1L).build();
        when(accountMapper.toDto(Mockito.<AccountEntity>any())).thenReturn(buildResult);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        when(accountRepository.existsByUserIdAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any())).thenReturn(false);
        when(accountRepository.save(any())).thenReturn(accountEntity);
        accountService.createAccount(new CreateAccountRequest());
        verify(accountRepository).existsByUserIdAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any());
        verify(accountMapper).toDto(Mockito.<AccountEntity>any());
        verify(accountRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#topUp(Long, TopUpRequest)}
     */
    @Test
    void testTopUp() {
        AccountDto.AccountDtoBuilder builderResult = AccountDto.builder();
        AccountDto.AccountDtoBuilder balanceResult = builderResult.balance(new BigDecimal("2.3"));
        AccountDto.AccountDtoBuilder statusResult = balanceResult.createdAt(LocalDate.of(1970, 1, 1).atStartOfDay())
                .currency(Currency.AZN)
                .iban("Iban")
                .id(1L)
                .status(AccountStatus.ACTIVATED);
        AccountDto buildResult = statusResult.updatedAt(LocalDate.of(1970, 1, 1).atStartOfDay()).userId(1L).build();
        when(accountMapper.toDto(Mockito.<AccountEntity>any())).thenReturn(buildResult);
        AccountEntity accountEntity = mock(AccountEntity.class);
        when(accountEntity.getBalance()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(accountEntity).setBalance(any());
        doNothing().when(accountEntity).setCurrency(any());
        doNothing().when(accountEntity).setIban(any());
        doNothing().when(accountEntity).setStatus(any());
        doNothing().when(accountEntity).setUserId(Mockito.<Long>any());
        doNothing().when(accountEntity).setCreatedAt(any());
        doNothing().when(accountEntity).setId(Mockito.<Long>any());
        doNothing().when(accountEntity).setUpdatedAt(any());
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setBalance(new BigDecimal("2.3"));
        accountEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setCurrency(Currency.AZN);
        accountEntity2.setIban("Iban");
        accountEntity2.setId(1L);
        accountEntity2.setStatus(AccountStatus.ACTIVATED);
        accountEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setUserId(1L);
        when(accountRepository.save(any())).thenReturn(accountEntity2);
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any())).thenReturn(ofResult);

        TopUpRequest request = new TopUpRequest();
        request.setAmount(new BigDecimal("2.3"));
        accountService.topUp(1L, request);
        verify(accountEntity).getBalance();
        verify(accountEntity, atLeast(1)).setBalance(any());
        verify(accountEntity).setCurrency(any());
        verify(accountEntity).setIban(any());
        verify(accountEntity).setStatus(any());
        verify(accountEntity).setUserId(Mockito.<Long>any());
        verify(accountEntity).setCreatedAt(any());
        verify(accountEntity).setId(Mockito.<Long>any());
        verify(accountEntity).setUpdatedAt(any());
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any());
        verify(accountMapper).toDto(Mockito.<AccountEntity>any());
        verify(accountRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#topUp(Long, TopUpRequest)}
     */
    @Test
    void testTopUp2() {
        AccountEntity accountEntity = mock(AccountEntity.class);
        when(accountEntity.getBalance()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(accountEntity).setBalance(any());
        doNothing().when(accountEntity).setCurrency(any());
        doNothing().when(accountEntity).setIban(any());
        doNothing().when(accountEntity).setStatus(any());
        doNothing().when(accountEntity).setUserId(Mockito.<Long>any());
        doNothing().when(accountEntity).setCreatedAt(any());
        doNothing().when(accountEntity).setId(Mockito.<Long>any());
        doNothing().when(accountEntity).setUpdatedAt(any());
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);
        when(accountRepository.save(any()))
                .thenThrow(new AlreadyExistException("An error occurred"));
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any())).thenReturn(ofResult);

        TopUpRequest request = new TopUpRequest();
        request.setAmount(new BigDecimal("2.3"));
        assertThrows(AlreadyExistException.class, () -> accountService.topUp(1L, request));
        verify(accountEntity).getBalance();
        verify(accountEntity, atLeast(1)).setBalance(any());
        verify(accountEntity).setCurrency(any());
        verify(accountEntity).setIban(any());
        verify(accountEntity).setStatus(any());
        verify(accountEntity).setUserId(Mockito.<Long>any());
        verify(accountEntity).setCreatedAt(any());
        verify(accountEntity).setId(Mockito.<Long>any());
        verify(accountEntity).setUpdatedAt(any());
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any());
        verify(accountRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#purchase(Long, PurchaseRequest)}
     */
    @Test
    void testPurchase() {
        PurchaseDto.PurchaseDtoBuilder builderResult = PurchaseDto.builder();
        PurchaseDto.PurchaseDtoBuilder idResult = builderResult.createdAt(LocalDate.of(1970, 1, 1).atStartOfDay())
                .currency(Currency.AZN)
                .iban("Iban")
                .id(1L);
        PurchaseDto.PurchaseDtoBuilder statusResult = idResult.price(new BigDecimal("2.3"))
                .productName("Product Name")
                .status(PurchaseStatus.APPROVED);
        PurchaseDto buildResult = statusResult.updatedAt(LocalDate.of(1970, 1, 1).atStartOfDay()).userId(1L).build();
        when(purchaseMapper.toDto(Mockito.<PurchaseEntity>any())).thenReturn(buildResult);
        AccountEntity accountEntity = mock(AccountEntity.class);
        when(accountEntity.getBalance()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(accountEntity).setBalance(any());
        doNothing().when(accountEntity).setCurrency(any());
        doNothing().when(accountEntity).setIban(any());
        doNothing().when(accountEntity).setStatus(any());
        doNothing().when(accountEntity).setUserId(Mockito.<Long>any());
        doNothing().when(accountEntity).setCreatedAt(any());
        doNothing().when(accountEntity).setId(Mockito.<Long>any());
        doNothing().when(accountEntity).setUpdatedAt(any());
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setBalance(new BigDecimal("2.3"));
        accountEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setCurrency(Currency.AZN);
        accountEntity2.setIban("Iban");
        accountEntity2.setId(1L);
        accountEntity2.setStatus(AccountStatus.ACTIVATED);
        accountEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setUserId(1L);
        when(accountRepository.save(any())).thenReturn(accountEntity2);
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatusAndBalanceGreaterThanEqual(Mockito.<Long>any(),
                any(), any(), any(), any()))
                .thenReturn(ofResult);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setCurrency(Currency.AZN);
        purchaseEntity.setIban("Iban");
        purchaseEntity.setId(1L);
        purchaseEntity.setPrice(new BigDecimal("2.3"));
        purchaseEntity.setProductName("Product Name");
        purchaseEntity.setStatus(PurchaseStatus.APPROVED);
        purchaseEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setUserId(1L);
        when(purchaseRepository.save(any())).thenReturn(purchaseEntity);

        PurchaseRequest request = new PurchaseRequest();
        request.setPrice(new BigDecimal("2.3"));
        accountService.purchase(1L, request);
        verify(accountEntity).getBalance();
        verify(accountEntity, atLeast(1)).setBalance(any());
        verify(accountEntity).setCurrency(any());
        verify(accountEntity).setIban(any());
        verify(accountEntity).setStatus(any());
        verify(accountEntity).setUserId(Mockito.<Long>any());
        verify(accountEntity).setCreatedAt(any());
        verify(accountEntity).setId(Mockito.<Long>any());
        verify(accountEntity).setUpdatedAt(any());
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatusAndBalanceGreaterThanEqual(Mockito.<Long>any(),
                any(), any(), any(), any());
        verify(purchaseMapper).toDto(Mockito.<PurchaseEntity>any());
        verify(accountRepository).save(any());
        verify(purchaseRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#purchase(Long, PurchaseRequest)}
     */
    @Test
    void testPurchase2() {
        AccountEntity accountEntity = mock(AccountEntity.class);
        when(accountEntity.getBalance()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(accountEntity).setBalance(any());
        doNothing().when(accountEntity).setCurrency(any());
        doNothing().when(accountEntity).setIban(any());
        doNothing().when(accountEntity).setStatus(any());
        doNothing().when(accountEntity).setUserId(Mockito.<Long>any());
        doNothing().when(accountEntity).setCreatedAt(any());
        doNothing().when(accountEntity).setId(Mockito.<Long>any());
        doNothing().when(accountEntity).setUpdatedAt(any());
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setBalance(new BigDecimal("2.3"));
        accountEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setCurrency(Currency.AZN);
        accountEntity2.setIban("Iban");
        accountEntity2.setId(1L);
        accountEntity2.setStatus(AccountStatus.ACTIVATED);
        accountEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setUserId(1L);
        when(accountRepository.save(any())).thenReturn(accountEntity2);
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatusAndBalanceGreaterThanEqual(Mockito.<Long>any(),
                any(), any(), any(), any()))
                .thenReturn(ofResult);
        when(purchaseRepository.save(any()))
                .thenThrow(new AlreadyExistException("An error occurred"));

        PurchaseRequest request = new PurchaseRequest();
        request.setPrice(new BigDecimal("2.3"));
        assertThrows(AlreadyExistException.class, () -> accountService.purchase(1L, request));
        verify(accountEntity).getBalance();
        verify(accountEntity, atLeast(1)).setBalance(any());
        verify(accountEntity).setCurrency(any());
        verify(accountEntity).setIban(any());
        verify(accountEntity).setStatus(any());
        verify(accountEntity).setUserId(Mockito.<Long>any());
        verify(accountEntity).setCreatedAt(any());
        verify(accountEntity).setId(Mockito.<Long>any());
        verify(accountEntity).setUpdatedAt(any());
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatusAndBalanceGreaterThanEqual(Mockito.<Long>any(),
                any(), any(), any(), any());
        verify(accountRepository).save(any());
        verify(purchaseRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#refund(Long, RefundRequest)}
     */
    @Test
    void testRefund() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setBalance(new BigDecimal("2.3"));
        accountEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setCurrency(Currency.AZN);
        accountEntity2.setIban("Iban");
        accountEntity2.setId(1L);
        accountEntity2.setStatus(AccountStatus.ACTIVATED);
        accountEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setUserId(1L);
        when(accountRepository.save(any())).thenReturn(accountEntity2);
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any())).thenReturn(ofResult);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setCurrency(Currency.AZN);
        purchaseEntity.setIban("Iban");
        purchaseEntity.setId(1L);
        purchaseEntity.setPrice(new BigDecimal("2.3"));
        purchaseEntity.setProductName("Product Name");
        purchaseEntity.setStatus(PurchaseStatus.APPROVED);
        purchaseEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setUserId(1L);
        Optional<PurchaseEntity> ofResult2 = Optional.of(purchaseEntity);

        PurchaseEntity purchaseEntity2 = new PurchaseEntity();
        purchaseEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity2.setCurrency(Currency.AZN);
        purchaseEntity2.setIban("Iban");
        purchaseEntity2.setId(1L);
        purchaseEntity2.setPrice(new BigDecimal("2.3"));
        purchaseEntity2.setProductName("Product Name");
        purchaseEntity2.setStatus(PurchaseStatus.APPROVED);
        purchaseEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity2.setUserId(1L);
        when(purchaseRepository.save(any())).thenReturn(purchaseEntity2);
        when(purchaseRepository.findByIdAndStatus(Mockito.<Long>any(), any()))
                .thenReturn(ofResult2);
        accountService.refund(1L, new RefundRequest());
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any());
        verify(purchaseRepository).findByIdAndStatus(Mockito.<Long>any(), any());
        verify(accountRepository).save(any());
        verify(purchaseRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#refund(Long, RefundRequest)}
     */
    @Test
    void testRefund2() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setBalance(new BigDecimal("2.3"));
        accountEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setCurrency(Currency.AZN);
        accountEntity2.setIban("Iban");
        accountEntity2.setId(1L);
        accountEntity2.setStatus(AccountStatus.ACTIVATED);
        accountEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setUserId(1L);
        when(accountRepository.save(any())).thenReturn(accountEntity2);
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any())).thenReturn(ofResult);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setCurrency(Currency.AZN);
        purchaseEntity.setIban("Iban");
        purchaseEntity.setId(1L);
        purchaseEntity.setPrice(new BigDecimal("2.3"));
        purchaseEntity.setProductName("Product Name");
        purchaseEntity.setStatus(PurchaseStatus.APPROVED);
        purchaseEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setUserId(1L);
        Optional<PurchaseEntity> ofResult2 = Optional.of(purchaseEntity);
        when(purchaseRepository.save(any()))
                .thenThrow(new AlreadyExistException("An error occurred"));
        when(purchaseRepository.findByIdAndStatus(Mockito.<Long>any(), any()))
                .thenReturn(ofResult2);
        assertThrows(AlreadyExistException.class, () -> accountService.refund(1L, new RefundRequest()));
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any());
        verify(purchaseRepository).findByIdAndStatus(Mockito.<Long>any(), any());
        verify(accountRepository).save(any());
        verify(purchaseRepository).save(any());
    }

    /**
     * Method under test: {@link AccountService#refund(Long, RefundRequest)}
     */
    @Test
    void testRefund3() {
        AccountEntity accountEntity = mock(AccountEntity.class);
        when(accountEntity.getBalance()).thenReturn(new BigDecimal("2.3"));
        doNothing().when(accountEntity).setBalance(any());
        doNothing().when(accountEntity).setCurrency(any());
        doNothing().when(accountEntity).setIban(any());
        doNothing().when(accountEntity).setStatus(any());
        doNothing().when(accountEntity).setUserId(Mockito.<Long>any());
        doNothing().when(accountEntity).setCreatedAt(any());
        doNothing().when(accountEntity).setId(Mockito.<Long>any());
        doNothing().when(accountEntity).setUpdatedAt(any());
        accountEntity.setBalance(new BigDecimal("2.3"));
        accountEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setCurrency(Currency.AZN);
        accountEntity.setIban("Iban");
        accountEntity.setId(1L);
        accountEntity.setStatus(AccountStatus.ACTIVATED);
        accountEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity.setUserId(1L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setBalance(new BigDecimal("2.3"));
        accountEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setCurrency(Currency.AZN);
        accountEntity2.setIban("Iban");
        accountEntity2.setId(1L);
        accountEntity2.setStatus(AccountStatus.ACTIVATED);
        accountEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        accountEntity2.setUserId(1L);
        when(accountRepository.save(any())).thenReturn(accountEntity2);
        when(accountRepository.findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any())).thenReturn(ofResult);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setCurrency(Currency.AZN);
        purchaseEntity.setIban("Iban");
        purchaseEntity.setId(1L);
        purchaseEntity.setPrice(new BigDecimal("2.3"));
        purchaseEntity.setProductName("Product Name");
        purchaseEntity.setStatus(PurchaseStatus.APPROVED);
        purchaseEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity.setUserId(1L);
        Optional<PurchaseEntity> ofResult2 = Optional.of(purchaseEntity);

        PurchaseEntity purchaseEntity2 = new PurchaseEntity();
        purchaseEntity2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity2.setCurrency(Currency.AZN);
        purchaseEntity2.setIban("Iban");
        purchaseEntity2.setId(1L);
        purchaseEntity2.setPrice(new BigDecimal("2.3"));
        purchaseEntity2.setProductName("Product Name");
        purchaseEntity2.setStatus(PurchaseStatus.APPROVED);
        purchaseEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        purchaseEntity2.setUserId(1L);
        when(purchaseRepository.save(any())).thenReturn(purchaseEntity2);
        when(purchaseRepository.findByIdAndStatus(Mockito.<Long>any(), any()))
                .thenReturn(ofResult2);
        accountService.refund(1L, new RefundRequest());
        verify(accountEntity).getBalance();
        verify(accountEntity, atLeast(1)).setBalance(any());
        verify(accountEntity).setCurrency(any());
        verify(accountEntity).setIban(any());
        verify(accountEntity).setStatus(any());
        verify(accountEntity).setUserId(Mockito.<Long>any());
        verify(accountEntity).setCreatedAt(any());
        verify(accountEntity).setId(Mockito.<Long>any());
        verify(accountEntity).setUpdatedAt(any());
        verify(accountRepository).findByUserIdAndIbanAndCurrencyAndStatus(Mockito.<Long>any(), any(),
                any(), any());
        verify(purchaseRepository).findByIdAndStatus(Mockito.<Long>any(), any());
        verify(accountRepository).save(any());
        verify(purchaseRepository).save(any());
    }

}
