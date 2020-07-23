package com.grouppage.security.jwt;

import com.grouppage.domain.entity.User;
import com.grouppage.domain.notmapped.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    private final String ROLES_KEY = "roles";
    private final String secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public final String accessCookieName;
    public final String refreshCookieName;

    public JwtProvider(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expiration}")long accessTokenExpirationTime,
            @Value("${security.jwt.token.refresh.expiration}")long refreshTokenExpirationTime,
            @Value("${security.jwt.token.accessTokenCookieName}")String accessCookieName,
            @Value("${security.jwt.token.refreshTokenCookieName}")String refreshCookieName
    ) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
        this.accessCookieName = accessCookieName;
        this.refreshCookieName = refreshCookieName;
    }

    public Token generateToken(User user, boolean access){
        Date[] dates = this.generateDatesForToken(access);
        Claims claims = this.generateClaimsFromUser(user);
        String token = this.generateTokenString(claims, dates[0], dates[1]);
        Token result = new Token();
        result.setTokenType(access ? Token.TokenType.ACCESS:
                Token.TokenType.REFRESH);
        result.setDuration((access ?
                accessTokenExpirationTime:
                refreshTokenExpirationTime)
                /1000);
        result.setTokenValue(token);
        result.setExpiryDate(LocalDateTime.ofInstant(dates[1].toInstant(), ZoneId.systemDefault()));
        return result;
    }
    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public List<GrantedAuthority> getRoles(String token){
        // TODO check if it works
        return Jwts.parser().setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(ROLES_KEY, List.class);
    }
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    public HttpCookie generateCookieFromToken(Token token){
        String encryptedTokenValue = SecurityCipher.encrypt(token.getTokenValue());
        String name = (token.getTokenType().equals(Token.TokenType.ACCESS)?
                this.accessCookieName :
                this.refreshCookieName);
        return ResponseCookie.from(name, encryptedTokenValue)
                .maxAge(token.getDuration())
                .secure(false)
                .httpOnly(true)
                .path("/")
                .build();
    }
    private Date[] generateDatesForToken(boolean access){
        Date now = new Date();
        Date expiryDate;
        if(access) {
            expiryDate = new Date(now.getTime() + this.accessTokenExpirationTime);
        }else {
            expiryDate = new Date(now.getTime() + this.refreshTokenExpirationTime);
        }
        return new Date[]{now, expiryDate};
    }
    private String generateTokenString(Claims claims, Date now, Date expiryDate){
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }
    private Claims generateClaimsFromUser(User user){
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put(this.ROLES_KEY, user.getAuthorities());
        return claims;
    }

    public HttpHeaders deleteJwtCookies() {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.SET_COOKIE,
                this.generateCookieFromToken(
                        this.generateTokenForDeletion(true)
                ).toString());
        header.add(HttpHeaders.SET_COOKIE,
                this.generateCookieFromToken(
                        this.generateTokenForDeletion(false)
                ).toString());
        return header;
    }
    private Token generateTokenForDeletion(boolean access){
        return new Token(access ?
                Token.TokenType.ACCESS:Token.TokenType.REFRESH,
                "",
                0L,
                null);
    }
}
