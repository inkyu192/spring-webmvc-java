package com.toy.shop.business.category.controller;

import com.toy.shop.common.ApiResponseDto;
import com.toy.shop.business.category.dto.CategoryDto;
import com.toy.shop.business.category.service.CategoryService;
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
    public Object saveCategory(@RequestBody @Valid CategoryDto.Save requestDto) {
        CategoryDto.Response responseDto = categoryService.saveCategory(requestDto);

        return new ApiResponseDto<>(responseDto);
    }

    @GetMapping
    public Object categories(@PageableDefault(size = 3) Pageable pageable) {
        Page<CategoryDto.Response> page = categoryService.categories(pageable);

        return new ApiResponseDto<>(page);
    }

    @GetMapping("/{id}")
    public Object category(@PathVariable Long id) {
        CategoryDto.Response responseDto = categoryService.category(id);

        return new ApiResponseDto<>(responseDto);
    }

    @PatchMapping("/{id}")
    public Object updateCategory(@PathVariable Long id,
                                 @RequestBody @Valid CategoryDto.Update requestDto) {
        CategoryDto.Response responseDto = categoryService.updateCategory(id, requestDto);

        return new ApiResponseDto<>(responseDto);
    }

    @DeleteMapping("/{id}")
    public Object deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return new ApiResponseDto<>();
    }
}
