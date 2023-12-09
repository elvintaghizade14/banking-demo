package com.azercell.banking.identity.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.azercell.banking.commonlib.exception.AuthException;
import com.azercell.banking.commonlib.exception.InvalidInputException;
import com.azercell.banking.identity.model.dto.TokenPairDto;
import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.dto.request.LoginRequest;
import com.azercell.banking.identity.model.enums.UserRole;
import com.azercell.banking.identity.model.enums.UserStatus;
import com.azercell.banking.identity.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthService.class})
@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RedisService redisService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link AuthService#login(LoginRequest)}
     */
    @Test
    void testLogin() {
        UserDto buildResult = UserDto.builder()
                .firstName("Jane")
                .id(1L)
                .lastName("Doe")
                .password("iloveyou")
                .phoneNumber("6625550144")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .username("janedoe")
                .build();
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(buildResult);
        doNothing().when(redisService).save(Mockito.<String>any(), Mockito.<TokenPairDto>any());
        TokenPairDto buildResult2 = TokenPairDto.builder().accessToken("ABC123").refreshToken("ABC123").build();
        when(tokenProvider.createTokenPair(Mockito.<UserDto>any())).thenReturn(buildResult2);
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        authService.login(new LoginRequest("janedoe", "iloveyou"));
        verify(tokenProvider).createTokenPair(Mockito.<UserDto>any());
        verify(redisService).save(Mockito.<String>any(), Mockito.<TokenPairDto>any());
        verify(userService).findByUsername(Mockito.<String>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#login(LoginRequest)}
     */
    @Test
    void testLogin2() {
        UserDto buildResult = UserDto.builder()
                .firstName("Jane")
                .id(1L)
                .lastName("Doe")
                .password("iloveyou")
                .phoneNumber("6625550144")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .username("janedoe")
                .build();
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(buildResult);
        when(tokenProvider.createTokenPair(Mockito.<UserDto>any())).thenThrow(new AuthException("0123456789ABCDEF"));
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthException.class, () -> authService.login(new LoginRequest("janedoe", "iloveyou")));
        verify(tokenProvider).createTokenPair(Mockito.<UserDto>any());
        verify(userService).findByUsername(Mockito.<String>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#login(LoginRequest)}
     */
    @Test
    void testLogin3() {
        UserDto buildResult = UserDto.builder()
                .firstName("Jane")
                .id(1L)
                .lastName("Doe")
                .password("iloveyou")
                .phoneNumber("6625550144")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .username("janedoe")
                .build();
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(buildResult);
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(false);
        assertThrows(AuthException.class, () -> authService.login(new LoginRequest("janedoe", "iloveyou")));
        verify(userService).findByUsername(Mockito.<String>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(String)}
     */
    @Test
    void testRefreshToken() {
        UserDto buildResult = UserDto.builder()
                .firstName("Jane")
                .id(1L)
                .lastName("Doe")
                .password("iloveyou")
                .phoneNumber("6625550144")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .username("janedoe")
                .build();
        when(userService.findByUsername(Mockito.<String>any())).thenReturn(buildResult);
        doNothing().when(redisService).update(Mockito.<String>any(), Mockito.<TokenPairDto>any());
        TokenPairDto buildResult2 = TokenPairDto.builder().accessToken("ABC123").refreshToken("ABC123").build();
        when(redisService.read(Mockito.<String>any())).thenReturn(buildResult2);
        TokenPairDto buildResult3 = TokenPairDto.builder().accessToken("ABC123").refreshToken("ABC123").build();
        when(tokenProvider.createTokenPair(Mockito.<UserDto>any())).thenReturn(buildResult3);
        when(tokenProvider.getUsernameFromToken(Mockito.<String>any())).thenReturn("janedoe");
        when(tokenProvider.isValidToken(Mockito.<String>any())).thenReturn(true);
        authService.refreshToken("ABC123");
        verify(tokenProvider).createTokenPair(Mockito.<UserDto>any());
        verify(tokenProvider).getUsernameFromToken(Mockito.<String>any());
        verify(tokenProvider).isValidToken(Mockito.<String>any());
        verify(redisService).read(Mockito.<String>any());
        verify(redisService).update(Mockito.<String>any(), Mockito.<TokenPairDto>any());
        verify(userService).findByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(String)}
     */
    @Test
    void testRefreshToken2() {
        when(tokenProvider.getUsernameFromToken(Mockito.<String>any()))
                .thenThrow(new InvalidInputException("An error occurred", "An error occurred"));
        when(tokenProvider.isValidToken(Mockito.<String>any())).thenReturn(true);
        assertThrows(InvalidInputException.class, () -> authService.refreshToken("ABC123"));
        verify(tokenProvider).getUsernameFromToken(Mockito.<String>any());
        verify(tokenProvider).isValidToken(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(String)}
     */
    @Test
    void testRefreshToken3() {
        TokenPairDto buildResult = TokenPairDto.builder().accessToken("ABC123").refreshToken("Refresh Token").build();
        when(redisService.read(Mockito.<String>any())).thenReturn(buildResult);
        when(tokenProvider.getUsernameFromToken(Mockito.<String>any())).thenReturn("janedoe");
        when(tokenProvider.isValidToken(Mockito.<String>any())).thenReturn(true);
        assertThrows(AuthException.class, () -> authService.refreshToken("ABC123"));
        verify(tokenProvider).getUsernameFromToken(Mockito.<String>any());
        verify(tokenProvider).isValidToken(Mockito.<String>any());
        verify(redisService).read(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#refreshToken(String)}
     */
    @Test
    void testRefreshToken4() {
        when(tokenProvider.isValidToken(Mockito.<String>any())).thenReturn(false);
        assertThrows(InvalidInputException.class, () -> authService.refreshToken("ABC123"));
        verify(tokenProvider).isValidToken(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#logout(String)}
     */
    @Test
    void testLogout() {
        doNothing().when(redisService).delete(Mockito.<String>any());
        authService.logout("janedoe");
        verify(redisService).delete(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthService#logout(String)}
     */
    @Test
    void testLogout2() {
        doThrow(new AuthException("0123456789ABCDEF")).when(redisService).delete(Mockito.<String>any());
        assertThrows(AuthException.class, () -> authService.logout("janedoe"));
        verify(redisService).delete(Mockito.<String>any());
    }
}
