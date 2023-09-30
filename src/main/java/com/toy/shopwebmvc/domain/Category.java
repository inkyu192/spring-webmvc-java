package com.toy.shopwebmvc.domain;

import com.toy.shopwebmvc.dto.request.CategorySaveRequest;
import com.toy.shopwebmvc.dto.request.CategoryUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseDomain {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    private String description;

//    @OneToMany(mappedBy = "category")
//    private List<Item> items = new ArrayList<>();

    public static Category createCategory(CategorySaveRequest categorySaveRequest) {
        Category category = new Category();
        category.name = categorySaveRequest.name();
        category.description = categorySaveRequest.description();

        return category;
    }

    public void updateCategory(CategoryUpdateRequest categoryUpdateRequest) {
        if (StringUtils.hasText(categoryUpdateRequest.name())) this.name = categoryUpdateRequest.name();
    }
}
