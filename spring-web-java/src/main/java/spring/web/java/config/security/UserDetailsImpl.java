package spring.web.java.config.security;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.web.java.constant.Role;
import spring.web.java.domain.Member;

import java.util.Collection;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long memberId;
    private final String account;
    private final String password;
    private final Role role;

    public UserDetailsImpl(Member member) {
        this.memberId = member.getId();
        this.account = member.getAccount();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    public UserDetailsImpl(Claims claims) {
        this.memberId = Long.valueOf(String.valueOf(claims.get("memberId")));
        this.account = String.valueOf(claims.get("account"));
        this.password = null;
        this.role = Role.of(claims.get("role"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
