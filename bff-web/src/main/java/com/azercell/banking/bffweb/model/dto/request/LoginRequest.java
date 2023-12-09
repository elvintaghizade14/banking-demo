package com.azercell.banking.bffweb.model.dto.request;

import com.azercell.banking.commonlib.exception.validation.constraint.Password;
import com.azercell.banking.commonlib.exception.validation.constraint.Username;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Username
    private String username;

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
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

}
