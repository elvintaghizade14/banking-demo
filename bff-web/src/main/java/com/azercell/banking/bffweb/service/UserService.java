package com.azercell.banking.bffweb.service;

import com.azercell.banking.bffweb.client.identity.IdentityClient;
import com.azercell.banking.bffweb.model.dto.UserDto;
import com.azercell.banking.bffweb.model.dto.request.CreateUserRequest;
import com.azercell.banking.bffweb.model.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IdentityClient identityClient;

    public UserDto createUser(CreateUserRequest request) {
        return identityClient.createUser(request);
    }

    public UserDto getUserById(final long id) {
        return identityClient.getUserById(id);
    }

    public UserDto getUserByUsername(final String username) {
        return identityClient.getUserByUsername(username);
    }

    public PageResponse<UserDto> getAllUsers(final int pageNumber, final int pageSize) {
        return identityClient.getAllUsers(pageNumber, pageSize);
    }

}
