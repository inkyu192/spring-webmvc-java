package spring.webmvc.domain.model.entity;

import java.util.Objects;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.converter.ProductExposureAttributeConverter;
import spring.webmvc.domain.model.enums.ProductCategory;
import spring.webmvc.domain.model.enums.ProductStatus;
import spring.webmvc.domain.model.vo.ProductExposureAttribute;
import spring.webmvc.infrastructure.exception.InvalidEntityStatusException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseCreator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ProductCategory category;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	private String name;

	private String description;

	private Long price;

	private Long quantity;

	@Convert(converter = ProductExposureAttributeConverter.class)
	private ProductExposureAttribute exposureAttribute;

	public static Product create(
		ProductCategory category,
		String name,
		String description,
		Long price,
		Long quantity,
		ProductExposureAttribute exposureAttribute
	) {
		Product product = new Product();

		product.category = category;
		product.name = name;
		product.description = description;
		product.price = price;
		product.quantity = quantity;
		product.exposureAttribute = exposureAttribute;

		return product;
	}

	public void update(
		ProductStatus status,
		String name,
		String description,
		Long price,
		Long quantity,
		ProductExposureAttribute exposureAttribute
	) {
		if (this.status != ProductStatus.PENDING) {
			throw new InvalidEntityStatusException(
				Product.class,
				Objects.requireNonNull(id),
				this.status.name(),
				status.name()
			);
		}

		this.status = status;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.exposureAttribute = exposureAttribute;
	}

	public void removeQuantity(Long quantity) {
		this.quantity -= quantity;
	}

	public void addQuantity(Long quantity) {
		this.quantity += quantity;
	}
}
