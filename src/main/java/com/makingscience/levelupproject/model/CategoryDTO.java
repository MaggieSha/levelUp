package com.makingscience.levelupproject.model;

import com.makingscience.levelupproject.model.entities.postgre.Category;
import lombok.*;

@Setter
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;


    public static CategoryDTO of(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;

    }
}
