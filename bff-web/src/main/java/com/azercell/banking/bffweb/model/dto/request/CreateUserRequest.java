package com.azercell.banking.bffweb.model.dto.request;

import com.azercell.banking.bffweb.model.enums.UserRole;
import com.azercell.banking.commonlib.exception.validation.constraint.Password;
import com.azercell.banking.commonlib.exception.validation.constraint.Username;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Builder
@ToString(exclude = {"password"})
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank
    @Username
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private UserRole role;

    @Password
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateUserRequest that = (CreateUserRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(phoneNumber, that.phoneNumber) && role == that.role
                && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, phoneNumber, role, password);
    }
}
