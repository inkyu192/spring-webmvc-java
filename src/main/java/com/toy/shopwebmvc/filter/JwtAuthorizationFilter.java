package com.toy.shopwebmvc.filter;


import com.toy.shopwebmvc.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenService tokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(authenticationManager);
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        try {
            String accessToken = null;

            String token = request.getHeader("Authorization");
            if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
                accessToken = token.replace("Bearer ", "");
            }

            if (StringUtils.hasText(accessToken)) {
                Authentication authentication = tokenService.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            //예외 별 응답 메시지 처리
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
