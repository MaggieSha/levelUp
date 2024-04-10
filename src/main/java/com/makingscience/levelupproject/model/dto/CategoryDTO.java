package com.makingscience.levelupproject.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.makingscience.levelupproject.model.entities.postgre.Category;
import lombok.*;

@Setter
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Long id;
    private String name;
    private Double commission;


    public static CategoryDTO of(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;

    }
    public static CategoryDTO toDTOWithCommission(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCommission(category.getCommission());
        return dto;

    }
}
