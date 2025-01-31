package spring.web.java.global.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import spring.web.java.domain.member.dto.MemberEvent;

@Slf4j
@Component
public class MemberEventListener {

	@SneakyThrows
	@Async
	@EventListener
	public void test(MemberEvent memberEvent) {
		Thread.sleep(3000);
		log.info("MemberEventListener start");
		log.info("account: {}", memberEvent.account());
		log.info("name: {}", memberEvent.name());
		log.info("MemberEventListener end");
	}
}
