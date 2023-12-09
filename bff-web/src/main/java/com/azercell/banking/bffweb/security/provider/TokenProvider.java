package com.azercell.banking.bffweb.security.provider;

import com.azercell.banking.bffweb.config.properties.ApplicationProperties;
import com.azercell.banking.bffweb.model.CustomUserPrincipal;
import com.azercell.banking.bffweb.model.constant.TokenKey;
import com.azercell.banking.bffweb.model.enums.UserRole;
import com.azercell.banking.bffweb.model.enums.UserStatus;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;

import static com.azercell.banking.commonlib.model.constant.CommonConstants.HttpAttribute.BEARER;
import static com.azercell.banking.commonlib.model.constant.CommonConstants.HttpHeader.AUTHORIZATION;

@Log4j2
@Component
public class TokenProvider {

    private final PublicKey jwtPublicKey;

    @SneakyThrows
    public TokenProvider(ApplicationProperties applicationProperties) {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Base64.Decoder decoder = Base64.getDecoder();
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder
                .decode(applicationProperties.getSecurity().getAuthentication().getPublicKey()));
        jwtPublicKey = keyFactory.generatePublic(publicKeySpec);
    }

    public boolean isValidToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtPublicKey).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT token compact of handler are invalid");
        } catch (JwtException e) {
            log.warn("Invalid JWT token");
        }
        return false;
    }

    public Authentication parseAuthentication(String authToken) {
        final Claims claims = extractClaim(authToken);
        final User principal = getPrincipal(claims);
        return new UsernamePasswordAuthenticationToken(principal, authToken, principal.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION);
        return extractToken(authHeader);
    }

    public String extractToken(final String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith(BEARER)
                || StringUtils.isBlank(authorizationHeader.replace(BEARER, ""))) {
            return null;
        }
        return authorizationHeader.replace(BEARER, "").trim();
    }

    private Claims extractClaim(String authToken) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtPublicKey)
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    private CustomUserPrincipal getPrincipal(Claims claims) {
        final String subject = claims.getSubject();
        final Long id = claims.get(TokenKey.ID, Long.class);
        final String tokenType = claims.get(TokenKey.TOKEN_TYPE, String.class);
        final UserRole userRole = UserRole.valueOf(claims.get(TokenKey.USER_ROLE, String.class));
        final UserStatus userStatus = UserStatus.valueOf(claims.get(TokenKey.USER_STATUS, String.class));

        return new CustomUserPrincipal(subject, id, userStatus, userRole, tokenType,
                Collections.singleton(new SimpleGrantedAuthority(userRole.name())));
    }
}
