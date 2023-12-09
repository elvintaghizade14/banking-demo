package com.azercell.banking.bffweb.model.dto;

import com.azercell.banking.bffweb.model.enums.UserRole;
import com.azercell.banking.bffweb.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRole role;
    private UserStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(username, userDto.username)
                && Objects.equals(firstName, userDto.firstName)
                && Objects.equals(lastName, userDto.lastName)
                && Objects.equals(phoneNumber, userDto.phoneNumber) && role == userDto.role
                && status == userDto.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, phoneNumber, role, status);
    }

}
