package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.CategoryDTO;
import com.makingscience.levelupproject.model.entities.postgre.Category;
import com.makingscience.levelupproject.model.enums.CategoryStatus;
import com.makingscience.levelupproject.model.params.CreateCategoryParam;
import com.makingscience.levelupproject.model.params.UpdateCategoryParam;
import com.makingscience.levelupproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryFacade {
    private final CategoryService categoryService;

    public CategoryDTO add(CreateCategoryParam param) {
        Optional<Category> optional = categoryService.findByName(param.getName());
        if(optional.isPresent())  {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Category with name " + param.getName() + " already exists!");
        }
        Category category = new Category();
        category.setName(param.getName());
        category.setCommission(param.getCommission());
        category.setCategoryStatus(CategoryStatus.ACTIVE);
        return CategoryDTO.of(categoryService.save(category));
    }

    public CategoryDTO update(UpdateCategoryParam param) {
        Category category = categoryService.findById(param.getId());
        category.setCommission(param.getCommission());

        return CategoryDTO.of(categoryService.save(category));
    }

    public void delete(Long id) {
        Category category = categoryService.findById(id);
        category.setCategoryStatus(CategoryStatus.DEACTIVATED);
        // ak tu romelime merchanti mooidzebneba am categoriit maso
        categoryService.save(category);
    }

    public CategoryDTO getById(Long id) {
        return CategoryDTO.of(categoryService.findById(id));

    }

    public Page<CategoryDTO> getAll(Pageable pageable) {
        Page<Category> page = categoryService.getAll(pageable);
        List<CategoryDTO> dtos = page.getContent().stream().map(category -> CategoryDTO.of(category)).collect(Collectors.toList());
        return new PageImpl<>(dtos,pageable,page.getTotalElements());
    }
}
