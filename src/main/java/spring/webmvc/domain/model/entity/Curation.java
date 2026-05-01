package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.webmvc.domain.converter.CurationAttributeConverter;
import spring.webmvc.domain.converter.CurationExposureAttributeConverter;
import spring.webmvc.domain.model.enums.CurationPlacement;
import spring.webmvc.domain.model.enums.CurationType;
import spring.webmvc.domain.model.vo.CurationAttribute;
import spring.webmvc.domain.model.vo.CurationExposureAttribute;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curation extends BaseCreator {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;

	@Enumerated(EnumType.STRING)
	private CurationPlacement placement;

	@Enumerated(EnumType.STRING)
	private CurationType type;

	@Convert(converter = CurationAttributeConverter.class)
	private CurationAttribute attribute;

	@Convert(converter = CurationExposureAttributeConverter.class)
	private CurationExposureAttribute exposureAttribute;

	private Boolean isExposed;
	private Long sortOrder;

	@OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<CurationProduct> curationProducts = new ArrayList<>();

	public List<CurationProduct> getCurationProducts() {
		return Collections.unmodifiableList(curationProducts);
	}

	public static Curation create(
		String title,
		CurationPlacement placement,
		CurationType type,
		CurationAttribute attribute,
		CurationExposureAttribute exposureAttribute,
		Boolean isExposed,
		Long sortOrder
	) {
		Curation curation = new Curation();

		curation.title = title;
		curation.placement = placement;
		curation.type = type;
		curation.attribute = attribute;
		curation.exposureAttribute = exposureAttribute;
		curation.isExposed = isExposed;
		curation.sortOrder = sortOrder;

		return curation;
	}

	public void addProduct(Product product, Long sortOrder) {
		CurationProduct curationProduct = CurationProduct.create(this, product, sortOrder);

		curationProducts.add(curationProduct);
	}
}
