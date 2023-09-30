package com.toy.shopwebmvc.repository;

import com.toy.shopwebmvc.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
