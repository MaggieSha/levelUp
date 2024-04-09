package com.makingscience.levelupproject.repository;

import java.util.UUID;

public interface FilterQueryResponse {
    Long getNumberOfSlots();
    String getDetailsJson();
    UUID getBranchId();

}
