package com.toy.shop.service;

import com.toy.shop.dto.request.RoleSaveRequest;
import com.toy.shop.dto.response.RoleResponse;
import com.toy.shop.repository.RoleRepository;
import com.toy.shop.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public RoleResponse saveRole(RoleSaveRequest roleSaveRequest) {
        Role role = Role.createRole(roleSaveRequest);

        roleRepository.save(role);

        return new RoleResponse(role);
    }
}
