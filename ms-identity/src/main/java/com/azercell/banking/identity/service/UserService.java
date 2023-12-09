package com.azercell.banking.identity.service;

import com.azercell.banking.commonlib.exception.AlreadyExistException;
import com.azercell.banking.commonlib.exception.DataNotFoundException;
import com.azercell.banking.identity.client.account.AccountClient;
import com.azercell.banking.identity.dao.entity.UserEntity;
import com.azercell.banking.identity.dao.repository.UserRepository;
import com.azercell.banking.identity.mapper.UserMapper;
import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.dto.request.CreateAccountRequest;
import com.azercell.banking.identity.model.dto.request.CreateAccountResponse;
import com.azercell.banking.identity.model.dto.request.CreateUserRequest;
import com.azercell.banking.identity.model.enums.AccountStatus;
import com.azercell.banking.identity.model.enums.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.azercell.banking.identity.exception.constant.ErrorMessage.USER_ALREADY_EXIST_MESSAGE;
import static com.azercell.banking.identity.exception.constant.ErrorMessage.USER_NOT_FOUND_MESSAGE;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserMapper userMapper;
    private final AccountClient accountClient;
    private final UserRepository userRepository;

    public UserDto findById(final long id) {
        return userMapper.toDto(findUserById(id));
    }

    public UserDto findByUsername(final String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> DataNotFoundException.of(USER_NOT_FOUND_MESSAGE, "username", username));
    }

    public Page<UserDto> findAll(final int pageNumber, final int pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        if (existsByUsername(request.getUsername())) {
            throw AlreadyExistException.of(USER_ALREADY_EXIST_MESSAGE, request.getUsername());
        }
        final UserEntity user = userMapper.toEntity(request);
        UserEntity userEntity = userRepository.save(user);
        CreateAccountResponse account = accountClient.createAccount(CreateAccountRequest.builder()
                .balance(BigDecimal.valueOf(100))
                .userId(user.getId())
                .currency(Currency.AZN)
                .status(AccountStatus.ACTIVATED)
                .build());
        log.info("Account created {}", account);
        return userMapper.toDto(userEntity);
    }

    // private methods

    private UserEntity findUserById(final long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                DataNotFoundException.of(USER_NOT_FOUND_MESSAGE, "userId", userId));
    }

    private boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

}
