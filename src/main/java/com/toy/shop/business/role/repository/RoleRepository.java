package com.toy.shop.business.role.repository;

import com.toy.shop.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
