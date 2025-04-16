package spring.webmvc.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.event.NotificationEvent;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.model.entity.Notification;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	@Transactional
	public void saveNotification(NotificationEvent notificationEvent) {
		Member member = memberRepository.findById(notificationEvent.memberId()).orElseThrow(RuntimeException::new);

		notificationRepository.save(
			Notification.of(
				member,
				notificationEvent.title(),
				notificationEvent.message(),
				notificationEvent.url()
			)
		);
	}
}
