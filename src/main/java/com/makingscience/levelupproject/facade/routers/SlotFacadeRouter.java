package com.makingscience.levelupproject.facade.routers;


import com.makingscience.levelupproject.facade.interfaces.SlotFacade;
import com.makingscience.levelupproject.model.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import com.makingscience.levelupproject.model.params.CreateSlotParam;
import com.makingscience.levelupproject.model.params.SlotFilterParam;
import com.makingscience.levelupproject.model.params.UpdateSlotParam;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotFacadeRouter {

    private final List<SlotFacade> slotFacades;
    private final BranchService branchService;
    private final SlotService slotService;
    private final JsonUtils jsonUtils;

    public SlotDTO add(CreateSlotParam param) {
        return chooseFacade(param.getBranchId()).add(param);
    }

    private SlotFacade chooseFacade(UUID branchId) {
        Branch branch = branchService.getById(branchId);
        return slotFacades.stream().filter(slotFacade -> slotFacade.getType().name().equals(branch.getMerchant().getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not supported category type: " + branch.getMerchant().getCategory().getName()));
    }

    public SlotDTO update(UpdateSlotParam param) {
        Slot slot = slotService.findById(param.getId());
        return chooseFacade(slot.getBranch().getId()).update(slot,param);
    }

    public void delete(Long id) {
        Slot slot = slotService.findById(id);

        Set<Reservation> reservationSet = slot.getReservationSet();
        for (Reservation reservation : reservationSet) {
            if (reservation.getReservationStatus().equals(ReservationStatus.PENDING)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }
        slot.setSlotStatus(SlotStatus.DELETED);
        slotService.save(slot);

    }

    public SlotDTO getById(Long id) {
        Slot slot = slotService.findById(id);
        return chooseFacade(slot.getBranch().getId()).getById(slot);
    }

    public Page<SlotDTO> filter(SlotFilterParam slotFilterParam, Pageable pageable) {
       if(slotFilterParam.getSlotFilterDetails()== null){
           Page<Slot> slots = slotService.findByBranchId(slotFilterParam.getBranchId(),pageable);
           List<SlotDTO> dtos = slots.stream().map(slot -> SlotDTO.of(slot,getDetails(slot))).collect(Collectors.toList());
           return new PageImpl<>(dtos,pageable,slots.getTotalElements());
       }
        Page<Slot> slots = chooseFacade(slotFilterParam.getBranchId()).filter(slotFilterParam,pageable);
        List<SlotDTO> dtos = slots.stream().map(slot -> SlotDTO.of(slot,getDetails(slot))).collect(Collectors.toList());
        return new PageImpl<>(dtos,pageable,slots.getTotalElements());
    }

    private SlotDetails getDetails(Slot slot) {
        return chooseFacade(slot.getBranch().getId()).toDetails(slot.getSlotDetails());


    }
}
