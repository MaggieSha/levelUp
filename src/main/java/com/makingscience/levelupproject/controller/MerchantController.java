package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.MerchantFacade;
import com.makingscience.levelupproject.model.MerchantDTO;
import com.makingscience.levelupproject.model.params.CreateMerchantParam;
import com.makingscience.levelupproject.model.params.UpdateMerchantParam;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
@Validated
@Slf4j
public class MerchantController {
    private final MerchantFacade merchantFacade;

    @PostMapping
    public ResponseEntity<MerchantDTO> add(@Valid @RequestBody CreateMerchantParam param) {
        MerchantDTO merchantDTO = merchantFacade.add(param);
        return ResponseEntity.ok(merchantDTO);
    }

    @PutMapping
    public ResponseEntity<MerchantDTO> update(@Valid @RequestBody UpdateMerchantParam param) {
        MerchantDTO merchantDTO = merchantFacade.update(param);
        return ResponseEntity.ok(merchantDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable UUID id) {
        merchantFacade.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> getById(@Valid @PathVariable UUID id) {
        MerchantDTO merchantDTO   = merchantFacade.getById(id);
        return ResponseEntity.ok(merchantDTO);
    }

    @GetMapping
    public ResponseEntity<Page<MerchantDTO>> getAllMerchants(Pageable pageable) {
        Page<MerchantDTO> merchantDTOS = merchantFacade.getAllMerchants(pageable);
        return ResponseEntity.ok(merchantDTOS);
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<Page<MerchantDTO>> getAllMerchantsByCategory(Pageable pageable,@PathVariable Long categoryId) {
        Page<MerchantDTO> merchantDTOS = merchantFacade.getAllMerchantsByCategory(pageable,categoryId);
        return ResponseEntity.ok(merchantDTOS);
    }

  //get popular merchants
    // TODO: get merchants by category, working hours,rating > ragacaze - > filter method
}
