package spring.web.java.filter;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import spring.web.java.common.JwtTokenProvider;
import spring.web.java.config.security.UserDetailsImpl;

import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String accessToken = getAccessToken(request);

        if (StringUtils.hasText(accessToken)) {
            Claims claims = jwtTokenProvider.parseAccessToken(accessToken);
            UserDetails userDetails = new UserDetailsImpl(claims);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    public String getAccessToken(HttpServletRequest request) {
        String accessToken = null;
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(token) && token.startsWith("Bearer")) {
            accessToken = token.replace("Bearer ", "");
        }

        return accessToken;
    }
}
