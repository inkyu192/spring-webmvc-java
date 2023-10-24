package com.toy.shopwebmvc.dto.response;

import com.toy.shopwebmvc.constant.Role;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long id,
        String account,
        String name,
        String city,
        String street,
        String zipcode,
        Role role
) {

}
