package com.toy.shopwebmvc.common;

import com.toy.shopwebmvc.dto.request.RoleSaveRequest;
import com.toy.shopwebmvc.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitApplicationRunner implements ApplicationRunner {

    private final RoleService roleService;

    @Override
    public void run(ApplicationArguments args) {
        roleService.saveRole(new RoleSaveRequest("ROLE_MEMBER", "사용자 권한", "사용자 권한"));
        roleService.saveRole(new RoleSaveRequest("ROLE_ADMIN", "관리자 권한", "관리자 권한"));
    }
}
