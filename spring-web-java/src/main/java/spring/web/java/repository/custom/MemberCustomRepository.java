package spring.web.java.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.web.java.domain.Member;

public interface MemberCustomRepository {

    Page<Member> findAllWithJpql(Pageable pageable, String account, String name);

    Page<Member> findAllWithQuerydsl(Pageable pageable, String account, String name);
}
