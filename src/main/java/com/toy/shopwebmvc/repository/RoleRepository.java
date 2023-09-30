package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
