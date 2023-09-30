package com.toy.shopwebmvc.service;

import com.toy.shopwebmvc.dto.request.RoleSaveRequest;
import com.toy.shopwebmvc.dto.response.RoleResponse;
import com.toy.shopwebmvc.repository.RoleRepository;
import com.toy.shopwebmvc.domain.Role;
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
