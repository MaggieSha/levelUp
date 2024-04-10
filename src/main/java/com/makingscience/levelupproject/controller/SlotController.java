package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.routers.SlotFacadeRouter;
import com.makingscience.levelupproject.model.dto.SlotDTO;
import com.makingscience.levelupproject.model.dto.SlotFilterDTO;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.params.CreateSlotParam;
import com.makingscience.levelupproject.model.params.SlotFilterParam;
import com.makingscience.levelupproject.model.params.UpdateSlotParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/slot")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SlotController {

    private final SlotFacadeRouter slotFacade;

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<SlotDTO> createSlot(@Valid @RequestBody CreateSlotParam param) {
        SlotDTO slotDTO = slotFacade.add(param);
        return ResponseEntity.ok(slotDTO);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteSlot(@Valid @PathVariable Long id) {
        slotFacade.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<SlotDTO> updateSlot(@Valid @RequestBody UpdateSlotParam param) {
        SlotDTO slotDTO = slotFacade.update(param);
        return ResponseEntity.ok(slotDTO);
    }


    @GetMapping("/merchant")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<SlotDTO>> getSlotsByMerchant(@RequestParam UUID merchantId, Pageable pageable) {
        Page<SlotDTO> slotDTO = slotFacade.getSlotsByMerchant(merchantId, pageable);
        return ResponseEntity.ok(slotDTO);
    }
    @GetMapping("/branch")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<SlotDTO>> getSlotsByBranch(@RequestParam UUID branchId, Pageable pageable) {
        Page<SlotDTO> slotDTO = slotFacade.getSlotsByBranch(branchId, pageable);
        return ResponseEntity.ok(slotDTO);
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<SlotDetails> getSlotDetails(@PathVariable Long id) {
        SlotDetails slotDetails = slotFacade.getSlotDetails(id);
        return ResponseEntity.ok(slotDetails);
    }

    @PostMapping("/filter")
//    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Page<SlotFilterDTO>> filter(@Valid @RequestBody SlotFilterParam slotFilterParam, Pageable pageable) {
        Page<SlotFilterDTO> slotDTO = slotFacade.filter(slotFilterParam, pageable);
        return ResponseEntity.ok(slotDTO);
    }


}
