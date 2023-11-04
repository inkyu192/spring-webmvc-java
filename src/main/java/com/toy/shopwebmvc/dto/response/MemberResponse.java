package com.toy.shopwebmvc.dto.response;

import com.toy.shopwebmvc.constant.Role;
import com.toy.shopwebmvc.domain.Member;

public record MemberResponse(
        Long id,
        String account,
        String name,
        String city,
        String street,
        String zipcode,
        Role role
) {
    public static MemberResponse create(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getAccount(),
                member.getName(),
                member.getAddress().getCity(),
                member.getAddress().getStreet(),
                member.getAddress().getCity(),
                member.getRole()
        );
    }
}
