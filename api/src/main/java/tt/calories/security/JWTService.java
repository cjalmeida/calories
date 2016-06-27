/*
 * Copyright (c) 2016. Cloves Almeida. All rights reserved.
 */

package tt.calories.security;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tt.calories.utilities.DateProvider;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom service and servlet filter for dealing with JWT tokens. See settings for security secret
 * and expiration date.
 *
 * If a valid token is found, it creates a pre-authenticated Authentication object in the security context.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Component
public class JWTService extends OncePerRequestFilter {


    Logger log = LoggerFactory.getLogger(JWTService.class);

    @Value("${security.secret}")
    String secret;

    @Value("${security.token.expiration}")
    Integer expirationDays;

    @Autowired
    private DateProvider dateProvider;

    private JWTVerifier verifier;
    private JWTSigner signer;

    @PostConstruct
    protected void init() {
        verifier = new JWTVerifier(secret);
        signer = new JWTSigner(secret);
    }

    /**
     * Process a HTTP request. If a JWT token is found and valid, it's credentials are put into
     * the security context.
     */
    protected void process(HttpServletRequest request, HttpServletResponse response) {
        String auth = request.getHeader("Authentication");
        if (auth == null) return;

        int found = auth.indexOf("Bearer ");
        if (found == -1) return;

        String token = auth.substring("Bearer ".length()).trim();
        try {
            Map<String, Object> data = verifier.verify(token);
            String username = (String) data.get("sub");
            Authentication authentication = new PreAuthenticatedAuthenticationToken(username, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.info("Exception when decoding JWT token.", e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        process(request, response);
        filterChain.doFilter(request, response);
    }

    /**
     * Creates a valid JWT token.
     */
    public String createToken(String username) {
        double expires = dateProvider.toJsonNumericDate(dateProvider.nowPlusDays(expirationDays));
        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", expires);
        claims.put("sub", username);
        return signer.sign(claims);
    }
}
