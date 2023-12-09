package com.azercell.banking.identity.service;

import com.azercell.banking.commonlib.exception.AlreadyExistException;
import com.azercell.banking.commonlib.exception.DataNotFoundException;
import com.azercell.banking.identity.client.account.AccountClient;
import com.azercell.banking.identity.dao.entity.UserEntity;
import com.azercell.banking.identity.dao.repository.UserRepository;
import com.azercell.banking.identity.mapper.UserMapper;
import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.dto.request.CreateUserRequest;
import com.azercell.banking.identity.model.enums.UserRole;
import com.azercell.banking.identity.model.enums.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    private AccountClient accountClient;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Method under test: {@link UserService#findById(long)}
     */
    @Test
    void testFindById() {
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
        when(userMapper.toDto(Mockito.<UserEntity>any())).thenReturn(buildResult);

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(UserRole.ROLE_USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userService.findById(1L);
        verify(userMapper).toDto(Mockito.<UserEntity>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#findById(long)}
     */
    @Test
    void testFindById2() {
        when(userMapper.toDto(Mockito.<UserEntity>any())).thenThrow(new AlreadyExistException("An error occurred"));

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(UserRole.ROLE_USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AlreadyExistException.class, () -> userService.findById(1L));
        verify(userMapper).toDto(Mockito.<UserEntity>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#findByUsername(String)}
     */
    @Test
    void testFindByUsername() {
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
        when(userMapper.toDto(Mockito.<UserEntity>any())).thenReturn(buildResult);

        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setFirstName("Jane");
        userEntity.setId(1L);
        userEntity.setLastName("Doe");
        userEntity.setPassword("iloveyou");
        userEntity.setPhoneNumber("6625550144");
        userEntity.setRole(UserRole.ROLE_USER);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(ofResult);
        userService.findByUsername("janedoe");
        verify(userRepository).findByUsername(Mockito.any());
        verify(userMapper).toDto(Mockito.<UserEntity>any());
    }

    /**
     * Method under test: {@link UserService#findAll(int, int)}
     */
    @Test
    void testFindAll() {
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        Page<UserDto> actualFindAllResult = userService.findAll(10, 3);
        verify(userRepository).findAll(Mockito.<Pageable>any());
        assertTrue(actualFindAllResult.toList().isEmpty());
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser() {
        when(userRepository.existsByUsername(Mockito.any()))
                .thenThrow(new DataNotFoundException("An error occurred"));
        assertThrows(DataNotFoundException.class, () -> userService
                .createUser(new CreateUserRequest("janedoe", "Jane", "Doe", "6625550144", UserRole.ROLE_USER, "iloveyou")));
        verify(userRepository).existsByUsername(Mockito.any());
    }
}