package com.azercell.banking.identity.dao.entity;


import com.azercell.banking.commonlib.exception.validation.constraint.Password;
import com.azercell.banking.identity.model.enums.UserRole;
import com.azercell.banking.identity.model.enums.UserStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "users")
@ToString(callSuper = true, exclude = {"password"})
public class UserEntity extends BaseEntity {
    private static final long serialVersionUID = 3470424543096822461L;

    @NotBlank
    @Column(name = "username", unique = true, length = 64)
    private String username;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 128)
    private UserStatus status;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @NotBlank
    @Column(name = "password")
    @Password
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName) && Objects.equals(phoneNumber, that.phoneNumber)
                && status == that.status && role == that.role && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, firstName, lastName, phoneNumber, status, role, password);
    }

}
