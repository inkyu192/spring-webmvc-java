package com.toy.shopwebmvc.common;

import com.toy.shopwebmvc.domain.Member;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final String account;
    private final String password;
    private final String roleId;

    public UserDetailsImpl(Member member) {
        this.account = member.getAccount();
        this.password = member.getPassword();
        this.roleId = member.getRole().getId();
    }

    public UserDetailsImpl(Claims claims) {
        this.account = claims.getSubject();
        this.password = null;
        this.roleId = claims.get("authorities").toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(roleId));
        return list;
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
