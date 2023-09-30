package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.domain.Member;

import java.util.List;

public interface MemberCustomRepository {

    List<Member> findAllOfJpql(String searchWord);

    List<Member> findAllOfQuery(String searchWord);
}
