package com.azercell.banking.identity.controller;

import com.azercell.banking.identity.model.dto.TokenPairDto;
import com.azercell.banking.identity.model.dto.request.LoginRequest;
import com.azercell.banking.identity.model.dto.request.RefreshTokenRequest;
import com.azercell.banking.identity.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.azercell.banking.commonlib.model.constant.CommonConstants.HttpHeader.X_USERNAME;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenPairDto> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPairDto> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest.getRefreshToken()));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(X_USERNAME) @NotBlank String username) {
        authService.logout(username);
        return ResponseEntity.ok().build();
    }

}
