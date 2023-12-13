package com.azercell.banking.account.controller;

import com.azercell.banking.account.model.dto.AccountDto;
import com.azercell.banking.account.model.dto.PurchaseDto;
import com.azercell.banking.account.model.dto.request.CreateAccountRequest;
import com.azercell.banking.account.model.dto.request.PurchaseRequest;
import com.azercell.banking.account.model.dto.request.RefundRequest;
import com.azercell.banking.account.model.dto.request.TopUpRequest;
import com.azercell.banking.account.model.enums.AccountStatus;
import com.azercell.banking.account.model.enums.Currency;
import com.azercell.banking.account.model.enums.PurchaseStatus;
import com.azercell.banking.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AccountController.class})
@ExtendWith(SpringExtension.class)
class AccountControllerTest {

    @Autowired
    private AccountController accountController;

    @MockBean
    private AccountService accountService;

    /**
     * Method under test:
     * {@link AccountController#createAccount(CreateAccountRequest)}
     */
    @Test
    void testCreateAccount() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBalance(new BigDecimal("2.3"));
        createAccountRequest.setCurrency(Currency.AZN);
        createAccountRequest.setStatus(AccountStatus.ACTIVATED);
        createAccountRequest.setUserId(1L);
        String content = (new ObjectMapper()).writeValueAsString(createAccountRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/internal/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(200));
    }

    /**
     * Method under test: {@link AccountController#topUp(Long, TopUpRequest)}
     */
    @Test
    void testTopUp() throws Exception {
        TopUpRequest topUpRequest = new TopUpRequest();
        topUpRequest.setAmount(new BigDecimal("2.3"));
        topUpRequest.setCurrency(Currency.AZN);
        topUpRequest.setIban("Iban");
        String content = (new ObjectMapper()).writeValueAsString(topUpRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/internal/accounts/top-up")
                .header("X-User-ID", "X-User-ID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AccountController#topUp(Long, TopUpRequest)}
     */
    @Test
    void testTopUp2() throws Exception {
        AccountDto.AccountDtoBuilder builderResult = AccountDto.builder();
        AccountDto.AccountDtoBuilder balanceResult = builderResult.balance(new BigDecimal("2.3"));
        AccountDto.AccountDtoBuilder statusResult = balanceResult.createdAt(LocalDate.of(1970, 1, 1).atStartOfDay())
                .currency(Currency.AZN)
                .iban("Iban")
                .id(1L)
                .status(AccountStatus.ACTIVATED);
        AccountDto buildResult = statusResult.updatedAt(LocalDate.of(1970, 1, 1).atStartOfDay()).userId(1L).build();
        when(accountService.topUp(Mockito.<Long>any(), Mockito.any())).thenReturn(buildResult);

        TopUpRequest topUpRequest = new TopUpRequest();
        topUpRequest.setAmount(new BigDecimal("2.3"));
        topUpRequest.setCurrency(Currency.AZN);
        topUpRequest.setIban("Iban");
        String content = (new ObjectMapper()).writeValueAsString(topUpRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/internal/accounts/top-up")
                .header("X-User-ID", 42)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"userId\":1,\"iban\":\"Iban\",\"currency\":\"AZN\",\"balance\":2.3,\"status\":\"ACTIVATED\",\"createdAt\":[1970"
                                        + ",1,1,0,0],\"updatedAt\":[1970,1,1,0,0]}"));
    }

    /**
     * Method under test: {@link AccountController#purchase(Long, PurchaseRequest)}
     */
    @Test
    void testPurchase() throws Exception {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCurrency(Currency.AZN);
        purchaseRequest.setIban("Iban");
        purchaseRequest.setPrice(new BigDecimal("2.3"));
        purchaseRequest.setProductName("Product Name");
        String content = (new ObjectMapper()).writeValueAsString(purchaseRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/internal/accounts/purchase")
                .header("X-User-ID", "X-User-ID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AccountController#purchase(Long, PurchaseRequest)}
     */
    @Test
    void testPurchase2() throws Exception {
        PurchaseDto.PurchaseDtoBuilder builderResult = PurchaseDto.builder();
        PurchaseDto.PurchaseDtoBuilder idResult = builderResult.createdAt(LocalDate.of(1970, 1, 1).atStartOfDay())
                .currency(Currency.AZN)
                .iban("Iban")
                .id(1L);
        PurchaseDto.PurchaseDtoBuilder statusResult = idResult.price(new BigDecimal("2.3"))
                .productName("Product Name")
                .status(PurchaseStatus.APPROVED);
        PurchaseDto buildResult = statusResult.updatedAt(LocalDate.of(1970, 1, 1).atStartOfDay()).userId(1L).build();
        when(accountService.purchase(Mockito.<Long>any(), Mockito.any())).thenReturn(buildResult);

        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCurrency(Currency.AZN);
        purchaseRequest.setIban("Iban");
        purchaseRequest.setPrice(new BigDecimal("2.3"));
        purchaseRequest.setProductName("Product Name");
        String content = (new ObjectMapper()).writeValueAsString(purchaseRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/internal/accounts/purchase")
                .header("X-User-ID", 42)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"userId\":1,\"iban\":\"Iban\",\"price\":2.3,\"currency\":\"AZN\",\"productName\":\"Product Name\",\"status\":"
                                        + "\"APPROVED\",\"createdAt\":[1970,1,1,0,0],\"updatedAt\":[1970,1,1,0,0]}"));
    }

    /**
     * Method under test: {@link AccountController#refund(Long, RefundRequest)}
     */
    @Test
    void testRefund() throws Exception {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setIban("Iban");
        refundRequest.setPurchaseId(1L);
        String content = (new ObjectMapper()).writeValueAsString(refundRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/internal/accounts/refund")
                .header("X-User-ID", "X-User-ID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link AccountController#refund(Long, RefundRequest)}
     */
    @Test
    void testRefund2() throws Exception {
        doNothing().when(accountService).refund(Mockito.<Long>any(), Mockito.any());

        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setIban("Iban");
        refundRequest.setPurchaseId(1L);
        String content = (new ObjectMapper()).writeValueAsString(refundRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/internal/accounts/refund")
                .header("X-User-ID", 42)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(accountController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }

}


