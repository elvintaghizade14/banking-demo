package com.azercell.banking.bffweb.service;

import com.azercell.banking.bffweb.client.account.AccountClient;
import com.azercell.banking.bffweb.model.dto.AccountDto;
import com.azercell.banking.bffweb.model.dto.PurchaseDto;
import com.azercell.banking.bffweb.model.dto.request.PurchaseRequest;
import com.azercell.banking.bffweb.model.dto.request.RefundRequest;
import com.azercell.banking.bffweb.model.dto.request.TopUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountClient accountClient;

    public List<AccountDto> getAccounts() {
        return accountClient.getAccounts();
    }

    public AccountDto getAccountByIban(String iban) {
        return accountClient.getAccountByIban(iban);
    }

    public AccountDto topUp(TopUpRequest request) {
        return accountClient.topUp(request);
    }

    public PurchaseDto purchase(PurchaseRequest request) {
        return accountClient.purchase(request);
    }

    public void refund(RefundRequest request) {
        accountClient.refund(request);
    }

}
