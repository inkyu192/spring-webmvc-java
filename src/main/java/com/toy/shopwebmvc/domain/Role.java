package com.toy.shopwebmvc.domain;

import com.toy.shopwebmvc.dto.request.RoleSaveRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseDomain {

    @Id
    @Column(name = "role_id")
    private String id;
    private String name;
    private String description;

//    @OneToMany(mappedBy = "role")
//    private List<Member> members = new ArrayList<>();

    public static Role createRole(RoleSaveRequest roleSaveRequest) {
        Role role = new Role();
        role.id = roleSaveRequest.id();
        role.name = roleSaveRequest.name();
        role.description = roleSaveRequest.description();

        return role;
    }
}
