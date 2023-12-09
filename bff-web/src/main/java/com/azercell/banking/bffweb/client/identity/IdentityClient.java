package com.azercell.banking.bffweb.client.identity;

import com.azercell.banking.bffweb.config.FeignConfig;
import com.azercell.banking.bffweb.model.dto.TokenPairDto;
import com.azercell.banking.bffweb.model.dto.UserDto;
import com.azercell.banking.bffweb.model.dto.request.CreateUserRequest;
import com.azercell.banking.bffweb.model.dto.request.LoginRequest;
import com.azercell.banking.bffweb.model.dto.request.RefreshTokenRequest;
import com.azercell.banking.bffweb.model.dto.response.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-identity", url = "${application.client.identity.url}/v1/internal",
        configuration = FeignConfig.class)
public interface IdentityClient {

    // -- AuthController --
    @PostMapping("/auth/login")
    TokenPairDto login(@RequestBody LoginRequest loginRequest);

    @PostMapping("/auth/refresh")
    TokenPairDto refresh(@RequestBody RefreshTokenRequest refreshTokenRequest);

    @DeleteMapping("/auth/logout")
    void logout();

    // -- UserController --
    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @GetMapping("/users/by")
    UserDto getUserByUsername(@RequestParam String username);

    @GetMapping("/users")
    PageResponse<UserDto> getAllUsers(@RequestParam int pageNumber, @RequestParam int pageSize);

    @PostMapping("/users")
    UserDto createUser(@RequestBody CreateUserRequest request);

}
