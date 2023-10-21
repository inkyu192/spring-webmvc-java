package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.domain.Member;
import com.toy.shopwebmvc.repository.custom.MemberCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    @Query("""
            SELECT m
            FROM Member m
            WHERE (:account IS NULL OR m.account LIKE CONCAT('%', :account, '%'))
            AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%'))
            """)
    Page<Member> findAll(Pageable pageable, @Param("account") String account, @Param("name") String name);

    @Query("""
            SELECT m
            FROM Member m
            WHERE m.account = :account
            """)
    Optional<Member> findByAccount(String account);
}
