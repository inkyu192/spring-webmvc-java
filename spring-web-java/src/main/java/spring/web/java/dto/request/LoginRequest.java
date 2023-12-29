package spring.web.java.dto.request;

public record LoginRequest(
        String account,
        String password
) {
}
