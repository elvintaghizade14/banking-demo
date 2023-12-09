package com.azercell.banking.identity.controller;

import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.dto.request.CreateUserRequest;
import com.azercell.banking.identity.model.enums.UserRole;
import com.azercell.banking.identity.model.enums.UserStatus;
import com.azercell.banking.identity.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser() throws Exception {
        when(userService.findAll(anyInt(), anyInt())).thenReturn(new PageImpl<>(new ArrayList<>()));

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstName("Jane");
        createUserRequest.setLastName("Doe");
        createUserRequest.setPassword("iloveyou");
        createUserRequest.setPhoneNumber("6625550144");
        createUserRequest.setRole(UserRole.ROLE_USER);
        createUserRequest.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(createUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link UserController#getAllUsers(int, int)}
     */
    @Test
    void testGetAllUsers() throws Exception {
        when(userService.findAll(anyInt(), anyInt())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/internal/users");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link UserController#getUserById(Long)}
     */
    @Test
    void testGetUserById() throws Exception {
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
        when(userService.findById(anyLong())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/internal/users/{id}", 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"username\":\"janedoe\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"role\":\"ROLE_USER\",\"status\""
                                        + ":\"ACTIVE\"}"));
    }

    /**
     * Method under test: {@link UserController#getUserByUsername(String)}
     */
    @Test
    void testGetUserByUsername() throws Exception {
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
        when(userService.findByUsername(Mockito.any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/internal/users/by")
                .param("username", "foo");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"username\":\"janedoe\",\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"role\":\"ROLE_USER\",\"status\""
                                        + ":\"ACTIVE\"}"));
    }
}
