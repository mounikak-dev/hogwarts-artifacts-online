package com.learn.hogwartsartifactsonline.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class UserRequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final UriTemplate USER_URI_TEMPLATE = new UriTemplate("/users/{userId}");

    @Override
    public @Nullable AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        //extract the userid from request uri
        Map<String, String> uriVariables = USER_URI_TEMPLATE.match(context.getRequest().getRequestURI());
        String userIdFromRequestUri = uriVariables.get("userId");

        //extract userId from authentication obj - a jwt object
        String UserIdFromJwt = ((Jwt)authenticationSupplier.get().getPrincipal()).getClaim("userId").toString();

        //check if user has the role user and role admin
        boolean hasUserRole = authenticationSupplier.get().
                getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_user"));
        boolean hasAdminRole =  authenticationSupplier.get().
                getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"));

        //compare two userIds
        boolean userIdsMatch = userIdFromRequestUri!=null && userIdFromRequestUri.equals(UserIdFromJwt);
        return new AuthorizationResult() {
            @Override
            public boolean isGranted() {
                return hasAdminRole || (hasUserRole && userIdsMatch);
            }
        };
    }
}
