package com.toy.shopwebmvc.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.dto.request.LoginRequest;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import com.toy.shopwebmvc.dto.response.TokenResponse;
import com.toy.shopwebmvc.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        LoginRequest loginRequest;

        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.account(), loginRequest.password());

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        String accessToken = tokenService.createAccessToken(authResult);
        String refreshToken = tokenService.createRefreshToken(authResult);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        String result = objectMapper.writeValueAsString(new ApiResponse<>(tokenResponse));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        String result = objectMapper.writeValueAsString(
                new ApiResponse<>(ApiResponseCode.BAD_REQUEST.name(), "인증 실패")
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
