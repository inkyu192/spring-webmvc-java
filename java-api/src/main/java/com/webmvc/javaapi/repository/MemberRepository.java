package com.webmvc.javaapi.repository;

import com.webmvc.javaapi.domain.Member;
import com.webmvc.javaapi.repository.custom.MemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    @Query("""
            SELECT m
            FROM Member m
            WHERE m.account = :account
            """)
    Optional<Member> findByAccount(String account);
}
