package com.toy.shop.repository;

import com.toy.shop.domain.Member;
import com.toy.shop.repository.custom.MemberCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    @Query(
        "select m" +
        " from Member m" +
        " where (:name is null or m.name like concat('%', :name, '%'))"
    )
    Page<Member> findAllOfQueryMethod(@Param("name") String name, Pageable pageable);

    @Query(
        "select m" +
        " from Member m" +
        " join fetch m.role r" +
        " where m.account = :account"
    )
    Optional<Member> findByAccount(String account);
}
