package com.azercell.banking.identity.security;

import com.azercell.banking.identity.config.properties.ApplicationProperties;
import com.azercell.banking.identity.model.constant.TokenKey;
import com.azercell.banking.identity.model.dto.TokenPairDto;
import com.azercell.banking.identity.model.dto.UserDto;
import com.azercell.banking.identity.model.enums.TokenType;
import com.azercell.banking.identity.util.FormatterUtil;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

@Log4j2
@Component
public class TokenProvider {

    private final PublicKey jwtPublicKey;
    private final PrivateKey jwtPrivateKey;
    private final ApplicationProperties applicationProperties;

    @SneakyThrows
    public TokenProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;

        final KeyFactory rsa = KeyFactory.getInstance("RSA");
        final Base64.Decoder decoder = Base64.getDecoder();
        final PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder
                .decode(applicationProperties.getSecurity().getAuthentication().getPrivateKey()));
        final X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder
                .decode(applicationProperties.getSecurity().getAuthentication().getPublicKey()));

        jwtPrivateKey = rsa.generatePrivate(privateKeySpec);
        jwtPublicKey = rsa.generatePublic(publicKeySpec);
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

    public TokenPairDto createTokenPair(UserDto user) {
        return TokenPairDto.builder()
                .accessToken(createToken(user, TokenType.ACCESS))
                .refreshToken(createToken(user, TokenType.REFRESH)).build();
    }

    private String createToken(UserDto user, TokenType tokenType) {
        final ApplicationProperties.Security.Authentication authenticationJwt =
                applicationProperties.getSecurity().getAuthentication();

        final Long tokenValidityInSeconds =
                (TokenType.ACCESS == tokenType) ? authenticationJwt.getAccessTokenValidityInSeconds() :
                        authenticationJwt.getRefreshTokenValidityInSeconds();
        final LocalDateTime validity = LocalDateTime.now().plusSeconds(tokenValidityInSeconds);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim(TokenKey.ID, user.getId())
                .claim(TokenKey.TOKEN_TYPE, tokenType)
                .claim(TokenKey.USER_ROLE, user.getRole().name())
                .claim(TokenKey.USER_STATUS, user.getStatus().name())
                .signWith(jwtPrivateKey, SignatureAlgorithm.RS256)
                .setExpiration(FormatterUtil.convertToUtilDate(validity))
                .compact();
    }

    public Claims extractClaim(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtPublicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(final String token) {
        final Claims claims = extractClaim(token);
        return claims.getSubject();
    }

}
