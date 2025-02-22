package spring.web.java.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.web.java.domain.repository.PermissionRepository;
import spring.web.java.presentation.dto.response.PermissionResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository permissionRepository;

	public List<PermissionResponse> findAll() {
		return permissionRepository.findAll().stream()
			.map(PermissionResponse::new)
			.toList();
	}
}
