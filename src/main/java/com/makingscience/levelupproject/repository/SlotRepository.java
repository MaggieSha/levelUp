package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotRepository extends JpaRepository<Slot, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s where s.id = :id")
    Optional<Slot> findByIdAndLock(Long id);

    Optional<Slot> findById(Long id);


    @Query("select s from Slot  s where s.externalId = :externalId and s.branch.id = :branchId and s.slotStatus = 'ACTIVE'")
    Optional<Slot> findByExternalIdAndBranchId(String externalId, UUID branchId);

    Optional<Slot> findByIdAndSlotStatus(Long id, SlotStatus slotStatus);

    @Query("select s from Slot  s where s.slotStatus =:slotStatus and s.branch.id = :branchId")
    Page<Slot> findByBranchIdAndStatus(UUID branchId, SlotStatus slotStatus, Pageable pageable);

    @Query(value = "SELECT count(*) as numberOfSlots,details as details,branch_id as branchId  FROM slot s " +
            "where status = 'ACTIVE' AND branch_id = :branchId group by branch_id,details", nativeQuery = true)
    Page<FilterQueryResponse> filterByBranchIdAndStatus(UUID branchId, Pageable pageable);

    @Query(value = "SELECT count(*) as numberOfSlots,details as details,branch_id as branchId  FROM slot s  WHERE " +
            "(:numberOfPeople is null or cast((details->>'tableCapacity') as integer) >= :numberOfPeople)" +
            "and status = 'ACTIVE' AND branch_id = :branchId " +
            "and (CAST(:preferredDay AS DATE) is null or NOT EXISTS (SELECT 1 FROM reservation r WHERE r.slot_id = s.id and r.reservation_day = CAST(:preferredDay AS DATE) and (r.status ='PENDING' or r.status ='CONFIRMED' or r.status ='CHECKED_IN') )) " +
            "group by branch_id,details", nativeQuery = true)
    Page<FilterQueryResponse> filterForRestaurant(Integer numberOfPeople, LocalDate preferredDay, UUID branchId, Pageable pageable);

    @Query(value = "SELECT *  FROM slot s  WHERE" +
            " (:numberOfPeople is null or cast((details->>'tableCapacity') as integer) >= :numberOfPeople) and " +
            "status = 'ACTIVE' AND branch_id = :branchId and " +
            "(CAST(:preferredDay AS DATE) is null or NOT EXISTS (SELECT 1 FROM reservation r WHERE r.slot_id = s.id and r.reservation_day = CAST(:preferredDay AS DATE) and (r.status ='PENDING' or r.status ='CONFIRMED' or r.status ='CHECKED_IN') )) ", nativeQuery = true)
    List<Slot> getAvailableSlotsForRestaurant(Integer numberOfPeople, LocalDate preferredDay, UUID branchId);

    @Query("select s from Slot  s where s.slotStatus =:slotStatus and s.branch.merchant.id = :merchantId")
    Page<Slot> findByMerchantIdAndStatus(UUID merchantId, SlotStatus slotStatus, Pageable pageable);



    @Query(value = "SELECT count(*) as numberOfSlots,details as details,branch_id as branchId  FROM slot s  " +
            "WHERE " +
            "(:serviceName is null or (details->>'serviceName') = :serviceName) and " +
            "(:hour is null or (details->>'visitHour')  = :hour) and " +
            "status = 'ACTIVE' AND branch_id = :branchId and " +
            "(CAST(:preferredDay AS DATE) is null or NOT EXISTS (SELECT 1 FROM reservation r WHERE r.slot_id = s.id and r.reservation_day = CAST(:preferredDay AS DATE) and (r.status ='PENDING' or r.status ='CONFIRMED' or r.status ='CHECKED_IN') )) " +
            "group by branch_id,details", nativeQuery = true)
    Page<FilterQueryResponse> filterForVisit(String serviceName, String hour, LocalDate preferredDay, UUID branchId, Pageable pageable);



    @Query(value = "SELECT * FROM slot s  " +
            "WHERE " +
            "(:serviceName is null or (details->>'serviceName') = :serviceName) and " +
            "(:hour is null or (details->>'visitHour')  = :hour) and " +
            "status = 'ACTIVE' AND branch_id = :branchId and " +
            "(CAST(:preferredDay AS DATE) is null or NOT EXISTS (SELECT 1 FROM reservation r WHERE r.slot_id = s.id and r.reservation_day = CAST(:preferredDay AS DATE) and (r.status ='PENDING' or r.status ='CONFIRMED' or r.status ='CHECKED_IN') )) ", nativeQuery = true)
    List<Slot> getAvailableSlotsForVisit(String serviceName, String hour, LocalDate preferredDay, UUID branchId);

    @Query(nativeQuery = true,value = "select * from slot  s where  branch_id = :branchId and status = 'ACTIVE' and " +
            "( ((details->>'serviceName') = :serviceName and  (details->>'visitHour') = :hour and (details->>'paidCancelledHours') = :paidHours) or external_id = :externalId) ")
    Optional<Slot> findByExternalIdAndBranchIdAndDetails(String externalId, UUID branchId, String serviceName,String hour,String paidHours);
}
