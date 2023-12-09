package com.azercell.banking.bffweb.controller;

import com.azercell.banking.bffweb.model.dto.UserDto;
import com.azercell.banking.bffweb.model.dto.request.CreateUserRequest;
import com.azercell.banking.bffweb.model.dto.response.PageResponse;
import com.azercell.banking.bffweb.service.UserService;
import com.azercell.banking.commonlib.exception.validation.constraint.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.azercell.banking.bffweb.model.constant.PageConstants.DEFAULT_PAGE_NUMBER;
import static com.azercell.banking.bffweb.model.constant.PageConstants.DEFAULT_PAGE_SIZE;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bff-web/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    // Followings are experimental services

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserByUsername(@NotBlank @Username @RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, required = false)
            @Min(0) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, required = false)
            @Min(1) int pageSize) {
        return ResponseEntity.ok(userService.getAllUsers(pageNumber, pageSize));
    }

}
