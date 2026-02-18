package spring.webmvc.domain.model.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private String departureLocation;

	private String arrivalLocation;

	private Instant departureTime;

	private Instant arrivalTime;

	public static Transport create(
		Product product,
		String departureLocation,
		String arrivalLocation,
		Instant departureTime,
		Instant arrivalTime
	) {
		Transport transport = new Transport();

		transport.product = product;
		transport.departureLocation = departureLocation;
		transport.arrivalLocation = arrivalLocation;
		transport.departureTime = departureTime;
		transport.arrivalTime = arrivalTime;

		return transport;
	}

	public void update(
		String departureLocation,
		String arrivalLocation,
		Instant departureTime,
		Instant arrivalTime
	) {
		this.departureLocation = departureLocation;
		this.arrivalLocation = arrivalLocation;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
	}
}
