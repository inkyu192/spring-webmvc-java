package com.toy.shop.business.category.service;

import com.toy.shop.business.category.dto.request.CategorySaveRequest;
import com.toy.shop.business.category.dto.request.CategoryUpdateRequest;
import com.toy.shop.business.category.dto.response.CategoryResponse;
import com.toy.shop.business.category.repository.CategoryRepository;
import com.toy.shop.common.ApiResponseCode;
import com.toy.shop.domain.Category;
import com.toy.shop.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse saveCategory(CategorySaveRequest categorySaveRequest) {
        Category category = Category.createCategory(categorySaveRequest);

        categoryRepository.save(category);

        return new CategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> categories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(CategoryResponse::new);
    }

    @Override
    public CategoryResponse category(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryResponse::new)
                .orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));

        category.updateCategory(categoryUpdateRequest);

        return new CategoryResponse(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }
}
