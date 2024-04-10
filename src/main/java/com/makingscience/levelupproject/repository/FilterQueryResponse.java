package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.details.slot.SlotDetails;

import java.util.UUID;

public interface FilterQueryResponse {
    Long getNumberOfSlots();
    String getDetails();
    UUID getBranchId();

}
