package com.toy.shop.common;

import com.toy.shop.domain.Member;
import com.toy.shop.domain.Role;
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

    private final Long id;
    private final String account;
    private final String password;
    private final String name;
    private final String roleId;

    public UserDetailsImpl(Member member) {
        this.id = member.getId();
        this.account = member.getAccount();
        this.password = member.getPassword();
        this.name = member.getName();
        this.roleId = member.getRole().getId();
    }

    public UserDetailsImpl(Claims claims) {
        this.id = Long.valueOf(String.valueOf(claims.get("id")));
        this.account = claims.getSubject();
        this.password = null;
        this.name = String.valueOf(claims.get("name"));
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
