package com.toy.shop.business.category.service;

import com.toy.shop.business.category.dto.request.CategorySaveRequest;
import com.toy.shop.business.category.dto.request.CategoryUpdateRequest;
import com.toy.shop.business.category.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse saveCategory(CategorySaveRequest categorySaveRequest);

    Page<CategoryResponse> categories(Pageable pageable);

    CategoryResponse category(Long id);

    CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest);

    void deleteCategory(Long id);
}
