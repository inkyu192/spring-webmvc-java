package com.toy.shop.domain;

import com.toy.shop.business.member.dto.request.MemberSaveRequest;
import com.toy.shop.business.member.dto.request.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseDomain {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String account;
    private String password;
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public static Member createMember(MemberSaveRequest requestDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.account = requestDto.account();
        member.password = passwordEncoder.encode(requestDto.password());
        member.name = requestDto.name();
        member.address = Address.createAddress((requestDto.city()), requestDto.street(), requestDto.zipcode());

        return member;
    }

    public void updateMember(MemberUpdateRequest requestDto, PasswordEncoder passwordEncoder) {
        if (StringUtils.hasText(requestDto.password())) this.name = passwordEncoder.encode(requestDto.password());
        if (StringUtils.hasText(requestDto.name())) this.name = requestDto.name();
        this.address.updateAddress((requestDto.city()), requestDto.street(), requestDto.zipcode());
    }
}
