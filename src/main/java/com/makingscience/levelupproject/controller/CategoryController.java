package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.CategoryFacade;
import com.makingscience.levelupproject.model.dto.CategoryDTO;
import com.makingscience.levelupproject.model.params.CreateCategoryParam;
import com.makingscience.levelupproject.model.params.UpdateCategoryParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryController {

    private final CategoryFacade categoryFacade;

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> add(@Valid @RequestBody CreateCategoryParam param) {
        CategoryDTO categoryDTO = categoryFacade.add(param);
        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> update(@Valid @RequestBody UpdateCategoryParam param) {
        CategoryDTO categoryDTO = categoryFacade.update(param);
        return ResponseEntity.ok(categoryDTO);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        categoryFacade.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> getById(@Valid @PathVariable Long id) {
        CategoryDTO categoryDTO = categoryFacade.getById(id);
        return ResponseEntity.ok(categoryDTO);

    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> get(Pageable pageable) {
        Page<CategoryDTO> categoryDTOs = categoryFacade.getAll(pageable);
        return ResponseEntity.ok(categoryDTOs);

    }
}
