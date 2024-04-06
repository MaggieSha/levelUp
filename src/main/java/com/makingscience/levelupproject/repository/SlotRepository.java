package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SlotRepository extends JpaRepository<Slot,Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s where s.id = :id")
    Optional<Slot> findByIdAndLock(Long id);

    Optional<Slot> findById(Long id);


    @Query("select s from Slot  s where s.externalId = :externalId and s.branch.id = :branchId and s.slotStatus = 'ACTIVE'")
    Optional<Slot> findByExternalIdAndBranchId(String externalId, UUID branchId);

    Optional<Slot> findByIdAndSlotStatus(Long id, SlotStatus slotStatus);
    @Query("select s from Slot  s where s.slotStatus =:slotStatus and s.branch.id = :branchId")
    Page<Slot> findByBranchIdAndStatus(UUID branchId, SlotStatus slotStatus, Pageable pageable);
}
