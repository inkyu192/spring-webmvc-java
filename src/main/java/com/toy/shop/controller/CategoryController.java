package com.toy.shop.controller;

import com.toy.shop.dto.request.CategorySaveRequest;
import com.toy.shop.dto.request.CategoryUpdateRequest;
import com.toy.shop.dto.response.CategoryResponse;
import com.toy.shop.service.CategoryService;
import com.toy.shop.dto.response.ApiResponse;
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

        return new ApiResponse<>(responseDto);
    }

    @GetMapping
    public Object categories(@PageableDefault(size = 3) Pageable pageable) {
        Page<CategoryResponse> page = categoryService.categories(pageable);

        return new ApiResponse<>(page);
    }

    @GetMapping("/{id}")
    public Object category(@PathVariable Long id) {
        CategoryResponse responseDto = categoryService.category(id);

        return new ApiResponse<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object updateCategory(@PathVariable Long id,
                                 @RequestBody @Valid CategoryUpdateRequest categoryUpdateRequest) {
        CategoryResponse responseDto = categoryService.updateCategory(id, categoryUpdateRequest);

        return new ApiResponse<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return new ApiResponse<>();
    }
}
