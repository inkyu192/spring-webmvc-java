package spring.webmvc.infrastructure.persistence.mongo;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import spring.webmvc.domain.model.document.Notification;
import spring.webmvc.infrastructure.config.MongoTestContainerConfig;

@DataMongoTest
@Import(MongoTestContainerConfig.class)
class NotificationMongoRepositoryTest {

	@Autowired
	private NotificationMongoRepository notificationMongoRepository;

	@Test
	@DisplayName("save: Notification 저장 후 반환한다")
	void save() {
		// Given
		Notification notification = Notification.create(1L, "title", "message", "url");

		// When
		Notification result = notificationMongoRepository.save(notification);

		// Then
		Assertions.assertThat(result.getId()).isNotNull();
		Assertions.assertThat(result.getMemberId()).isEqualTo(notification.getMemberId());
		Assertions.assertThat(result.getTitle()).isEqualTo(notification.getTitle());
		Assertions.assertThat(result.getMessage()).isEqualTo(notification.getMessage());
		Assertions.assertThat(result.getUrl()).isEqualTo(notification.getUrl());
	}

	@Test
	@DisplayName("findById: Notification 반환한다")
	void findById() {
		// Given
		Notification notification = notificationMongoRepository.save(
			Notification.create(1L, "title", "message", "url")
		);

		// When
		Optional<Notification> result = notificationMongoRepository.findById(notification.getId());

		// Then
		Assertions.assertThat(result).isPresent();
		Assertions.assertThat(result.get().getMemberId()).isEqualTo(notification.getMemberId());
		Assertions.assertThat(result.get().getTitle()).isEqualTo(notification.getTitle());
		Assertions.assertThat(result.get().getMessage()).isEqualTo(notification.getMessage());
		Assertions.assertThat(result.get().getUrl()).isEqualTo(notification.getUrl());
	}

	@Test
	@DisplayName("findAll: Notification 목록 반환한다")
	void findAll() {
		// Given
		notificationMongoRepository.save(Notification.create(1L, "title1", "msg1", "url1"));
		notificationMongoRepository.save(Notification.create(2L, "title2", "msg2", "url2"));

		// When
		List<Notification> result = notificationMongoRepository.findAll();

		// Then
		Assertions.assertThat(result).hasSize(2);
	}

	@Test
	@DisplayName("deleteById: Notification 삭제한다")
	void deleteById() {
		// Given
		Notification notification = notificationMongoRepository.save(
			Notification.create(1L, "title", "message", "url")
		);
		String id = notification.getId();

		// When
		notificationMongoRepository.deleteById(id);

		// Then
		Optional<Notification> deleted = notificationMongoRepository.findById(id);
		Assertions.assertThat(deleted).isEmpty();
	}
}
