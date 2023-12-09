package com.azercell.banking.bffweb.client.account;

import com.azercell.banking.bffweb.config.FeignConfig;
import com.azercell.banking.bffweb.model.dto.AccountDto;
import com.azercell.banking.bffweb.model.dto.PurchaseDto;
import com.azercell.banking.bffweb.model.dto.request.PurchaseRequest;
import com.azercell.banking.bffweb.model.dto.request.RefundRequest;
import com.azercell.banking.bffweb.model.dto.request.TopUpRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-account", url = "${application.client.account.url}/v1/internal/accounts",
        configuration = FeignConfig.class)
public interface AccountClient {

    @PutMapping("/top-up")
    AccountDto topUp(@RequestBody TopUpRequest request);

    @PutMapping("/purchase")
    PurchaseDto purchase(@RequestBody PurchaseRequest request);

    @PutMapping("/refund")
    void refund(@RequestBody RefundRequest request);

}
