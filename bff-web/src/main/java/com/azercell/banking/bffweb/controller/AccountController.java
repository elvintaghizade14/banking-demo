package com.azercell.banking.bffweb.controller;

import com.azercell.banking.bffweb.model.dto.AccountDto;
import com.azercell.banking.bffweb.model.dto.PurchaseDto;
import com.azercell.banking.bffweb.model.dto.request.PurchaseRequest;
import com.azercell.banking.bffweb.model.dto.request.RefundRequest;
import com.azercell.banking.bffweb.model.dto.request.TopUpRequest;
import com.azercell.banking.bffweb.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bff-web/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return ResponseEntity.ok(accountService.getAccounts());
    }

    @GetMapping("/by-iban")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AccountDto> getAccountByIban(@NotBlank @RequestParam String iban) {
        return ResponseEntity.ok(accountService.getAccountByIban(iban));
    }

    @PutMapping("/top-up")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<AccountDto> topUp(@Valid @RequestBody TopUpRequest request) {
        return ResponseEntity.ok(accountService.topUp(request));
    }

    @PutMapping("/purchase")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<PurchaseDto> purchase(@Valid @RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(accountService.purchase(request));
    }

    @PutMapping("/refund")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> refund(@Valid @RequestBody RefundRequest request) {
        accountService.refund(request);
        return ResponseEntity.accepted().build();
    }

}
