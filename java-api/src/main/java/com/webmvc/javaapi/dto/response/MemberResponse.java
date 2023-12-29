package com.webmvc.javaapi.dto.response;


import com.webmvc.javaapi.constant.Role;
import com.webmvc.javaapi.domain.Member;

public record MemberResponse(
        Long id,
        String account,
        String name,
        String city,
        String street,
        String zipcode,
        Role role
) {
    public MemberResponse(Member member) {
        this(
                member.getId(),
                member.getAccount(),
                member.getName(),
                member.getAddress().getCity(),
                member.getAddress().getStreet(),
                member.getAddress().getZipcode(),
                member.getRole()
        );
    }
}
