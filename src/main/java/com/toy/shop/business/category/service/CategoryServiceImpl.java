package com.toy.shop.business.category.service;

import com.toy.shop.common.ApiResponseCode;
import com.toy.shop.domain.Category;
import com.toy.shop.business.category.dto.CategoryDto;
import com.toy.shop.exception.CommonException;
import com.toy.shop.business.category.repository.CategoryRepository;
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
    public CategoryDto.Response saveCategory(CategoryDto.Save requestDto) {
        Category category = Category.createCategory(requestDto);

        categoryRepository.save(category);

        return new CategoryDto.Response(category);
    }

    @Override
    public Page<CategoryDto.Response> categories(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);

        return page.map(CategoryDto.Response::new);
    }

    @Override
    public CategoryDto.Response category(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));

        return new CategoryDto.Response(category);
    }

    @Override
    @Transactional
    public CategoryDto.Response updateCategory(Long id, CategoryDto.Update requestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));

        category.updateCategory(requestDto);

        return new CategoryDto.Response(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CommonException(ApiResponseCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(category);
    }
}
