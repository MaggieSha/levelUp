package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.SalonService;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import com.makingscience.levelupproject.repository.FilterQueryResponse;
import com.makingscience.levelupproject.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SlotService {
    private final SlotRepository slotRepository;

    public Slot save(Slot slot) {
        return slotRepository.save(slot);
    }

    public Optional<Slot> findByExternalIdAndBranchId(String externalId, UUID branchId) {
        return slotRepository.findByExternalIdAndBranchId(externalId,branchId);
    }

    public Slot findById(Long id) {
        return slotRepository.findByIdAndSlotStatus(id, SlotStatus.ACTIVE).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Slot with id " + id + "not found!"));
    }

    public Page<FilterQueryResponse> filterByBranchId(UUID branchId, Pageable pageable) {

        return slotRepository.filterByBranchIdAndStatus(branchId,pageable);

    }
    public Page<Slot> findByBranchId(UUID branchId, Pageable pageable) {
        return slotRepository.findByBranchIdAndStatus(branchId,SlotStatus.ACTIVE,pageable);
    }




 
    public Page<FilterQueryResponse> filterForRestaurant(Integer numberOfPeople, LocalDate preferredDay, UUID branchId, Pageable pageable) {
        return slotRepository.filterForRestaurant(numberOfPeople,preferredDay,branchId,pageable);
    }

    public Page<Slot> findByMerchantId(UUID merchantId, Pageable pageable) {
        return slotRepository.findByMerchantIdAndStatus(merchantId,SlotStatus.ACTIVE,pageable);
    }

    public Page<FilterQueryResponse> filterForSalon(SalonService serviceName, String stylistName, LocalTime hour, LocalDate preferredDay, UUID branchId, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return slotRepository.filterForSalon(serviceName!=null ? serviceName.name() : null,stylistName,hour !=null ? hour.format(formatter) : null,preferredDay,branchId,pageable);
    }

    public List<Slot> getAvailableSlotsForRestaurant(Integer numberOfPeople, LocalDate preferredDay, UUID branchId) {
        return slotRepository.getAvailableSlotsForRestaurant(numberOfPeople,preferredDay,branchId);
    }

    public List<Slot> getAvailableSlotsForSalon(SalonService serviceName, String stylistName, LocalTime preferredTime, LocalDate reservationDay, UUID branchId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return slotRepository.getAvailableSlotsForSalon(serviceName!=null ? serviceName.name() : null,stylistName,preferredTime.format(formatter),reservationDay,branchId);
    }
}
