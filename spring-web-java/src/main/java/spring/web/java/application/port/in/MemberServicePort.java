package spring.web.java.application.port.in;

import spring.web.java.dto.request.LoginRequest;
import spring.web.java.dto.request.MemberSaveRequest;
import spring.web.java.dto.request.MemberUpdateRequest;
import spring.web.java.dto.response.MemberResponse;
import spring.web.java.dto.response.TokenResponse;

public interface MemberServicePort {

	MemberResponse saveMember(MemberSaveRequest memberSaveRequest);

	TokenResponse login(LoginRequest loginRequest);

	MemberResponse findMember(Long id);

	MemberResponse updateMember(Long id, MemberUpdateRequest memberUpdateRequest);

	void deleteMember(Long id);
}
