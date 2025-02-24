package spring.web.java.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.web.java.domain.model.entity.Member;
import spring.web.java.domain.model.entity.Notification;
import spring.web.java.domain.repository.MemberRepository;
import spring.web.java.domain.repository.NotificationRepository;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationEventListener {

	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	@Async
	@EventListener
	@Transactional
	public void handleNotificationEvent(NotificationEvent notificationEvent) {
		try {
			Member member = memberRepository.findById(notificationEvent.memberId()).orElseThrow(RuntimeException::new);

			notificationRepository.save(
				Notification.of(
					member,
					notificationEvent.title(),
					notificationEvent.message(),
					notificationEvent.url()
				)
			);
		} catch (Exception e) {
			log.error("알림 이벤트 처리 중 예외 발생: {}", e.getMessage(), e);
		}
	}
}
