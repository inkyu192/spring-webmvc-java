package spring.web.java.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.web.java.constant.Role;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseDomain {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String account;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public static Member create(String account, String password, String name, Role role, Address address) {
        Member member = new Member();

        member.account = account;
        member.password = password;
        member.name = name;
        member.role = role;
        member.address = address;

        return member;
    }

    public void update(String name, Role role, Address address) {
        this.name = name;
        this.role = role;
        this.address = address;
    }
}
