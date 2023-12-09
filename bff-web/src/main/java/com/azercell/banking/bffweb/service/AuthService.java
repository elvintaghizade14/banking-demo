package com.azercell.banking.bffweb.service;

import com.azercell.banking.bffweb.client.identity.IdentityClient;
import com.azercell.banking.bffweb.model.dto.TokenPairDto;
import com.azercell.banking.bffweb.model.dto.request.LoginRequest;
import com.azercell.banking.bffweb.model.dto.request.RefreshTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IdentityClient identityClient;

    public TokenPairDto login(LoginRequest request) {
        return identityClient.login(request);
    }

    public void logout() {
        identityClient.logout();
    }

    public TokenPairDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return identityClient.refresh(refreshTokenRequest);
    }

}
