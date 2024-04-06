package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.SlotFacadeRouter;
import com.makingscience.levelupproject.model.SlotDTO;
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

@RestController
@RequestMapping("/api/slot")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SlotController {

    private final SlotFacadeRouter slotFacade;

    @PostMapping
    public ResponseEntity<SlotDTO> add(@Valid @RequestBody CreateSlotParam param) {
        SlotDTO slotDTO = slotFacade.add(param);
        return ResponseEntity.ok(slotDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlotDTO> getById(@Valid @PathVariable Long id) {
        SlotDTO slotDTO = slotFacade.getById(id);
        return ResponseEntity.ok(slotDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable Long id) {
        slotFacade.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SlotDTO> update(@Valid @RequestBody UpdateSlotParam param) {
        SlotDTO slotDTO = slotFacade.update(param);
        return ResponseEntity.ok(slotDTO);
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<SlotDTO>> filter(@Valid @RequestBody SlotFilterParam slotFilterParam, Pageable pageable) {
        Page<SlotDTO> slotDTO = slotFacade.filter(slotFilterParam,pageable);
        return ResponseEntity.ok(slotDTO);
    }




    // TODO: get all slots, get slots by id, get slots by branchId

    // filter to fetch all possible slots in category. this will return branches
}
