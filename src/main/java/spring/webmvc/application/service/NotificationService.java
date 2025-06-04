package spring.webmvc.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.webmvc.application.event.NotificationEvent;
import spring.webmvc.domain.model.document.Notification;
import spring.webmvc.domain.model.entity.Member;
import spring.webmvc.domain.repository.MemberRepository;
import spring.webmvc.domain.repository.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;

	public void createNotification(NotificationEvent notificationEvent) {
		Member member = memberRepository.findById(notificationEvent.memberId()).orElseThrow(RuntimeException::new);

		notificationRepository.save(
			Notification.create(
				member.getId(),
				notificationEvent.title(),
				notificationEvent.message(),
				notificationEvent.url()
			)
		);
	}
}
