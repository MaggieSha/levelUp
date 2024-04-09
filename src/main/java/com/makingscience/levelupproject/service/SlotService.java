package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.SlotFilterDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
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



    public List<Slot> getAvailableSlotsForRestaurant(Integer numberOfPeople, LocalDate preferredDay, UUID branchId) {
        return slotRepository.getAvailableSlotsForRestaurant(numberOfPeople,preferredDay,branchId);
    }

    public Page<FilterQueryResponse> filterForRestaurant(Integer numberOfPeople, LocalDate preferredDay, UUID branchId, Pageable pageable) {
        return slotRepository.filterForRestaurant(numberOfPeople,preferredDay,branchId,pageable);
    }

    public Page<Slot> findByMerchantId(UUID merchantId, Pageable pageable) {
        return slotRepository.findByMerchantIdAndStatus(merchantId,SlotStatus.ACTIVE,pageable);
    }

    public Page<FilterQueryResponse> filterForSalon(SalonService serviceName, String stylistName, int hour, LocalDate preferredDay, UUID branchId, Pageable pageable) {
        return slotRepository.filterForSalon(serviceName.name(),stylistName,hour,preferredDay,branchId,pageable);
    }
}
