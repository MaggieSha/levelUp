package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.enums.BranchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BranchRepository extends JpaRepository<Branch, UUID> {

    @Query("select b from Branch  b where b.contactPhone = :contactPhone and b.merchant.id <> :merchantId and b.status = 'ACTIVE'")
    Optional<Branch> getByContactPhoneAndMerchantId(String contactPhone, UUID merchantId);

    Optional<Branch> findByIdAndStatus(UUID id, BranchStatus branchStatus);


    @Query("select b from Branch  b where b.status = 'ACTIVE' and b.merchant.id = :merchantId")
    Page<Branch> getAllByMerchantId(Pageable pageable, UUID merchantId);
}
