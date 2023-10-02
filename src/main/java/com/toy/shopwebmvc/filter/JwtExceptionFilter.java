package com.toy.shopwebmvc.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.shopwebmvc.constant.ApiResponseCode;
import com.toy.shopwebmvc.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.toy.shopwebmvc.constant.ApiResponseCode.*;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (SignatureException e) {
            setResponse(response, "잘못된 토큰");
        } catch (MalformedJwtException e) {
            setResponse(response, "미지원 토큰");
        } catch (ExpiredJwtException e) {
            setResponse(response, "만료된 토큰");
        }
    }

    private void setResponse(HttpServletResponse response, String message) throws RuntimeException, IOException {
        String result = objectMapper.writeValueAsString(new ApiResponse<>(BAD_REQUEST.getCode(), message));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
    }
}
