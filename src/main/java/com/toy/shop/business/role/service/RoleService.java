package com.toy.shop.business.role.service;

import com.toy.shop.business.role.dto.request.RoleSaveRequest;
import com.toy.shop.business.role.dto.response.RoleResponse;

public interface RoleService {

    RoleResponse saveRole(RoleSaveRequest roleSaveRequest);
}
