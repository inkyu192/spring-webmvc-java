package spring.web.java.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    READY("준비"),
    CANCEL("취소"),
    COMP("완료");

    private final String description;
}
