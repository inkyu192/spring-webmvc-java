package com.toy.shopwebmvc.domain;

import com.toy.shopwebmvc.constant.Role;
import com.toy.shopwebmvc.dto.request.MemberSaveRequest;
import com.toy.shopwebmvc.dto.request.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseDomain {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String account;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public static Member create(MemberSaveRequest memberSaveRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.account = memberSaveRequest.account();
        member.password = passwordEncoder.encode(memberSaveRequest.password());
        member.name = member.getName();
        member.role = memberSaveRequest.role();
        member.address = Address.create(
                memberSaveRequest.city(),
                memberSaveRequest.street(),
                memberSaveRequest.zipcode()
        );

        return member;
    }

    public void update(MemberUpdateRequest memberUpdateRequest) {
        this.name = memberUpdateRequest.name();
        this.role = memberUpdateRequest.role();
        this.address = Address.create(
                memberUpdateRequest.city(),
                memberUpdateRequest.street(),
                memberUpdateRequest.zipcode()
        );
    }
}
