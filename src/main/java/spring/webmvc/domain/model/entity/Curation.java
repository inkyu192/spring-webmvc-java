package spring.webmvc.domain.model.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curation extends BaseCreator {

	@Id
	@GeneratedValue
	private Long id;
	private String title;
	private boolean isExposed;
	private long sortOrder;

	@OneToMany(mappedBy = "curation", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<CurationProduct> curationProducts = new ArrayList<>();

	public List<CurationProduct> getCurationProducts() {
		return Collections.unmodifiableList(curationProducts);
	}

	public static Curation create(String title, boolean isExposed, long sortOrder) {
		Curation curation = new Curation();

		curation.title = title;
		curation.isExposed = isExposed;
		curation.sortOrder = sortOrder;

		return curation;
	}

	public void addProduct(CurationProduct curationProduct) {
		curationProducts.add(curationProduct);
	}
}
