package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {

    Page<Member> findAllWithJpql(Pageable pageable, String account, String name);

    Page<Member> findAllWithQuerydsl(Pageable pageable, String account, String name);
}
