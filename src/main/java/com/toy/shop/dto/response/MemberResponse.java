package com.toy.shop.dto.response;

import com.toy.shop.domain.Member;

public record MemberResponse(
        Long id,
        String account,
        String name,
        String city,
        String street,
        String zipcode
) {
    public MemberResponse(Member member) {
        this(member.getId(), member.getAccount(), member.getName(), member.getAddress().getCity(),
                member.getAddress().getStreet(), member.getAddress().getZipcode());
    }
}
