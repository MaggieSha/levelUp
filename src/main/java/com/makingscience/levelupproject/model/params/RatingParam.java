package com.makingscience.levelupproject.model.params;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class RatingParam {
    @NotNull(message = "reservationId-Is required")
    private Long reservationId;

    @NotNull(message = "rating-Is required")
    private Double rating;
    private String comment;
}
