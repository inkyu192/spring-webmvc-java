package spring.web.java.application.port.in;

import spring.web.java.dto.request.TokenRequest;
import spring.web.java.dto.response.TokenResponse;

public interface TokenServicePort {

	TokenResponse reissue(TokenRequest tokenRequest);
}
