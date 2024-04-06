package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.CategoryDTO;
import com.makingscience.levelupproject.model.entities.postgre.Category;
import com.makingscience.levelupproject.model.enums.CategoryStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.CreateCategoryParam;
import com.makingscience.levelupproject.model.params.UpdateCategoryParam;
import com.makingscience.levelupproject.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }


    public Optional<Category> findByName(String name) {
        return categoryRepository.findByNameAndCategoryStatus(name, CategoryStatus.ACTIVE);
    }

    public Category findById(Long id) {
        return categoryRepository.findByIdAndCategoryStatus(id,CategoryStatus.ACTIVE).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found by id " + id));
    }



    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAllByCategoryStatus(CategoryStatus.ACTIVE,pageable);
    }
}
