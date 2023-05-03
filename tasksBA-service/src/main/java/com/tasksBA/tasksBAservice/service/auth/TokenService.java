package com.tasksBA.tasksBAservice.service.auth;

import com.tasksBA.tasksBAservice.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class TokenService {
    @Value("${jwt.secret}")
    public String jwtSecret;

    @Value("${jwt.expiration.minutes}")
    public Long expirationMinutes;

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "SERVER";
    public static final String TOKEN_AUDIENCE = "UI";

    public static final String TOKEN_HEADER = "authorization";

    public static final String TOKEN_PREFIX = "Bearer ";


    public String generate(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        byte[] signKey = jwtSecret.getBytes();

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .signWith(Keys.hmacShaKeyFor(signKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setId(UUID.randomUUID().toString())
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .claim("rol", roles)
                .compact();
    }

    public Optional<Jws<Claims>> validateToken(String token) {
        try {
            byte[] signKey = jwtSecret.getBytes();

            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build()
                    .parseClaimsJws(token);
            return Optional.of(jws);
        } catch (ExpiredJwtException exception) {
            log.error("Token {} has expired", token);
        } catch (UnsupportedJwtException exception) {
            log.error("Token {} is not supported", token);
        } catch (MalformedJwtException exception) {
            log.error("Token {} is malformed", token);
        } catch (IllegalArgumentException exception) {
            log.error("Token {} is not valid", token);
        }
        return Optional.empty();

    }

    public Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)){
            return Optional.of(tokenHeader.replace(TOKEN_PREFIX,""));
        }
        return Optional.empty();
    }

}
