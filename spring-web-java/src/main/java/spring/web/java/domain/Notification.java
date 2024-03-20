package spring.web.java.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends Base {

    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;
    private String type;
    private String title;
    private String message;
    private String url;
}
