package com.toy.shop.business.category.controller;

import com.toy.shop.business.category.dto.request.CategorySaveRequest;
import com.toy.shop.business.category.dto.request.CategoryUpdateRequest;
import com.toy.shop.business.category.dto.response.CategoryResponse;
import com.toy.shop.business.category.service.CategoryService;
import com.toy.shop.common.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Object saveCategory(@RequestBody @Valid CategorySaveRequest categorySaveRequest) {
        CategoryResponse responseDto = categoryService.saveCategory(categorySaveRequest);

        return new ApiResponseDto<>(responseDto);
    }

    @GetMapping
    public Object categories(@PageableDefault(size = 3) Pageable pageable) {
        Page<CategoryResponse> page = categoryService.categories(pageable);

        return new ApiResponseDto<>(page);
    }

    @GetMapping("/{id}")
    public Object category(@PathVariable Long id) {
        CategoryResponse responseDto = categoryService.category(id);

        return new ApiResponseDto<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object updateCategory(@PathVariable Long id,
                                 @RequestBody @Valid CategoryUpdateRequest categoryUpdateRequest) {
        CategoryResponse responseDto = categoryService.updateCategory(id, categoryUpdateRequest);

        return new ApiResponseDto<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return new ApiResponseDto<>();
    }
}
