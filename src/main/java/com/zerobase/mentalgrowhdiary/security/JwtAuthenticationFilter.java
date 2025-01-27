package com.zerobase.mentalgrowhdiary.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String token = resolveTokenFromRequest(request);

            if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
                Authentication auth = tokenProvider.getAuthentication(token);

                if(auth != null){
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("Auth Set in SecurityContextHolder for user: {}", auth.getName());
                }
            }

        } catch(Exception e){
            log.error("Failed to JWT Auth : ", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {

        String token = request.getHeader(TOKEN_HEADER);

        if(StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
