package com.recargapay.app.config.security;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @SuppressWarnings("unchecked")
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        // Extrai as roles do client "account"
        Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");

        // Evita NullPointerException se "realm_access" ou "roles" estiverem ausentes
        List<String> roles = Optional.ofNullable(account)
                .map(r -> (List<String>) r.get("roles"))
                .orElse(Collections.emptyList());

        List<GrantedAuthority> result = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
                .collect(Collectors.toList());

        log.debug("User Roles: {}", result);

        return result;
    }

    @Override
    public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
