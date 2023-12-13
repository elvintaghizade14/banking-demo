package com.azercell.banking.account.controller;

import com.azercell.banking.account.model.dto.AccountDto;
import com.azercell.banking.account.model.dto.PurchaseDto;
import com.azercell.banking.account.model.dto.request.CreateAccountRequest;
import com.azercell.banking.account.model.dto.request.PurchaseRequest;
import com.azercell.banking.account.model.dto.request.RefundRequest;
import com.azercell.banking.account.model.dto.request.TopUpRequest;
import com.azercell.banking.account.service.AccountService;
import com.azercell.banking.commonlib.model.constant.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }

    @GetMapping("/by-iban")
    public ResponseEntity<AccountDto> getAccountByIban(@NotBlank @RequestParam String iban) {
        return ResponseEntity.ok(accountService.getAccountByIban(iban));
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PutMapping("/top-up")
    public ResponseEntity<AccountDto> topUp(@NotNull @RequestHeader(CommonConstants.HttpHeader.X_USER_ID) Long userId,
                                            @Valid @RequestBody TopUpRequest request) {
        System.err.println(userId);
        return ResponseEntity.ok(accountService.topUp(userId, request));
    }

    @PutMapping("/purchase")
    public ResponseEntity<PurchaseDto> purchase(@NotNull @RequestHeader(CommonConstants.HttpHeader.X_USER_ID) Long userId,
                                                @Valid @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(accountService.purchase(userId, request));
    }

    @PutMapping("/refund")
    public ResponseEntity<Void> refund(@NotNull @RequestHeader(CommonConstants.HttpHeader.X_USER_ID) Long userId,
                                       @Valid @RequestBody RefundRequest request) {
        accountService.refund(userId, request);
        return ResponseEntity.accepted().build();
    }

}
