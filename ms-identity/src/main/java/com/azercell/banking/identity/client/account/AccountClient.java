package com.azercell.banking.identity.client.account;

import com.azercell.banking.commonlib.config.FeignConfig;
import com.azercell.banking.identity.model.dto.request.CreateAccountRequest;
import com.azercell.banking.identity.model.dto.request.CreateAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-account", url = "${application.client.account.url}/v1/internal",
        configuration = FeignConfig.class)
public interface AccountClient {

    @PostMapping("/accounts")
    CreateAccountResponse createAccount(@RequestBody CreateAccountRequest request);

}
