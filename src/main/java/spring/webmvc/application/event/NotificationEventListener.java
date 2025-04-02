package spring.webmvc.application.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Notification;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.NotificationRepository;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationEventListener {

	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	@Async
	@TransactionalEventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleNotificationEvent(NotificationEvent notificationEvent) {
		try {
			Thread.sleep(3000);

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
