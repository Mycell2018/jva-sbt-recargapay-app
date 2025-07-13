package com.recargapay.app.domain.linsteners;

import com.recargapay.app.domain.model.UserRevEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        UserRevEntity rev = (UserRevEntity) revisionEntity;

        String username = "anonymous";

        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                Jwt jwt = jwtAuth.getToken();

                username = jwt.getClaimAsString("preferred_username");
            }
        } catch (Exception ignored) {
        }

        rev.setUsername(username);
    }
}
