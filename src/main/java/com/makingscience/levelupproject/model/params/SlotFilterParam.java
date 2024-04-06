package com.makingscience.levelupproject.model.params;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@NoArgsConstructor
@Setter
@Getter
public class SlotFilterParam {

    private UUID branchId;
    private SlotFilterDetails slotFilterDetails;

}
