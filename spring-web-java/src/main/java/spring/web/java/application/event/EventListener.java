package spring.web.java.application.event;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventListener {

	@SneakyThrows
	@Async
	@org.springframework.context.event.EventListener
	public void test(MemberEvent memberEvent) {
		Thread.sleep(3000);
		log.info("MemberEventListener start");
		log.info("account: {}", memberEvent.account());
		log.info("name: {}", memberEvent.name());
		log.info("MemberEventListener end");
	}
}
