package com.azercell.banking.identity.service;

import com.azercell.banking.commonlib.exception.AuthException;
import com.azercell.banking.commonlib.exception.InvalidInputException;
import com.azercell.banking.identity.model.dto.TokenPairDto;
import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.dto.request.LoginRequest;
import com.azercell.banking.identity.model.enums.UserStatus;
import com.azercell.banking.identity.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.azercell.banking.commonlib.exception.constant.CommonErrorCode.REQUEST_INVALID;
import static com.azercell.banking.identity.exception.constant.ErrorMessage.*;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserService userService;
    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional(noRollbackFor = {AuthException.class})
    public TokenPairDto login(LoginRequest loginRequest) {
        final UserDto userDto = userService.findByUsername(loginRequest.getUsername());
        if (UserStatus.ACTIVE != userDto.getStatus()) {
            throw AuthException.of(USER_IS_NOT_ACTIVE_MESSAGE, userDto.getUsername(), userDto.getStatus().name());
        }
        if (passwordEncoder.matches(loginRequest.getPassword(), userDto.getPassword())) {
            final TokenPairDto tokenPairDto = tokenProvider.createTokenPair(userDto);
            redisService.save(userDto.getUsername(), tokenPairDto);
            return tokenPairDto;
        } else {
            throw new AuthException(UNAUTHORIZED_MESSAGE);
        }
    }

    public TokenPairDto refreshToken(final String refreshToken) {
        if (!tokenProvider.isValidToken(refreshToken)) {
            throw new InvalidInputException(REQUEST_INVALID, INVALID_TOKEN_MESSAGE);
        }
        final String username = tokenProvider.getUsernameFromToken(refreshToken);
        final TokenPairDto tokenPairDto = redisService.read(username);

        if (tokenPairDto == null || !Objects.equals(refreshToken, tokenPairDto.getRefreshToken())) {
            throw new AuthException(INVALID_TOKEN_MESSAGE);
        }
        final UserDto userDto = userService.findByUsername(username);
        final TokenPairDto newTokenPair = tokenProvider.createTokenPair(userDto);
        redisService.update(username, newTokenPair);
        return newTokenPair;
    }

    public void logout(final String username) {
        redisService.delete(username);
    }

}
