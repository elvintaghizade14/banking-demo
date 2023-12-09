package com.azercell.banking.bffweb.model;

import com.azercell.banking.bffweb.model.enums.UserRole;
import com.azercell.banking.bffweb.model.enums.UserStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomUserPrincipal extends User {

    private final Long id;
    private final String tokenType;
    private final UserRole userRole;
    private final UserStatus userStatus;

    public CustomUserPrincipal(String username, Long id, UserStatus userStatus, UserRole userRole, String tokenType,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, "", true, true, true, true, authorities);
        this.id = id;
        this.userRole = userRole;
        this.tokenType = tokenType;
        this.userStatus = userStatus;
    }

}
