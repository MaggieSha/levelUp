package com.makingscience.levelupproject.model.params;

import com.makingscience.levelupproject.model.enums.Type;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@NoArgsConstructor
@Getter
@ToString
public class UpdateCategoryParam {

    @NotNull(message = "commission-Is required")
    private Double commission;


    @NotNull(message = "id-Is required")
    private  Long id;

}
