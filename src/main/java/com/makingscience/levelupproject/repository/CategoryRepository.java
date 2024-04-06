package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Category;
import com.makingscience.levelupproject.model.enums.CategoryStatus;
import com.makingscience.levelupproject.model.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByNameAndCategoryStatus(String name, CategoryStatus categoryStatus);

    Optional<Category> findByIdAndCategoryStatus(Long id,CategoryStatus categoryStatus);

    Page<Category> findAllByCategoryStatus(CategoryStatus categoryStatus, Pageable pageable);
}
