package com.toy.shopwebmvc.repository.custom;

import com.toy.shopwebmvc.domain.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberCustomRepository {

    List<Member> findAllWithJpql(Pageable pageable, String account, String name);

    List<Member> findAllWithQuerydsl(Pageable pageable, String account, String name);
}
