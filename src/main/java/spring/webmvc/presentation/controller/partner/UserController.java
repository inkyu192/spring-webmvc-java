package spring.webmvc.presentation.controller.partner;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.dto.query.UserQuery;
import spring.webmvc.application.dto.result.UserCredentialResult;
import spring.webmvc.application.service.UserService;
import spring.webmvc.domain.model.entity.User;
import spring.webmvc.presentation.dto.response.OffsetPageResponse;
import spring.webmvc.presentation.dto.response.UserDetailResponse;
import spring.webmvc.presentation.dto.response.UserSummaryResponse;

@RestController
@RequestMapping("/partner/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	@PreAuthorize("hasAuthority('USER_READ')")
	public OffsetPageResponse<UserSummaryResponse> findUsers(
		@PageableDefault Pageable pageable,
		@RequestParam(required = false) String phone,
		@RequestParam(required = false) String name,
		@RequestParam Instant createdFrom,
		@RequestParam Instant createdTo
	) {
		UserQuery query = UserQuery.create(pageable, phone, name, createdFrom, createdTo);

		Page<User> page = userService.findUsers(query);

		return OffsetPageResponse.of(page, UserSummaryResponse::of);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('USER_READ')")
	public UserDetailResponse findUser(@PathVariable Long id) {
		UserCredentialResult result = userService.findUserDetail(id);

		return UserDetailResponse.of(result.user(), result.userCredential(), result.oAuths());
	}
}
