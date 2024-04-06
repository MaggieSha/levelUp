package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.BranchFacade;
import com.makingscience.levelupproject.model.BranchDTO;
import com.makingscience.levelupproject.model.MerchantDTO;
import com.makingscience.levelupproject.model.params.CreateBranchParam;
import com.makingscience.levelupproject.model.params.CreateMerchantParam;
import com.makingscience.levelupproject.model.params.UpdateBranchParam;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BranchController {

    private final BranchFacade branchFacade;

    @PostMapping
    public ResponseEntity<BranchDTO> add(@Valid @RequestBody CreateBranchParam param) {
        BranchDTO branchDTO = branchFacade.add(param);
        return ResponseEntity.ok(branchDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getById(@PathVariable UUID id) {
        BranchDTO branchDTO = branchFacade.getById(id);
        return ResponseEntity.ok(branchDTO);
    }

    @GetMapping("/byMerchant/{merchantId}")
    public ResponseEntity<Page<BranchDTO>> getAllBranchesByMerchant(Pageable pageable, @PathVariable UUID merchantId) {
        Page<BranchDTO> branchDTOs = branchFacade.getAllByMerchantId(pageable,merchantId);
        return ResponseEntity.ok(branchDTOs);
    }

    @PutMapping
    public ResponseEntity<BranchDTO> update(@Valid @RequestBody UpdateBranchParam param) {
        BranchDTO branchDTO = branchFacade.update(param);
        return ResponseEntity.ok(branchDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable UUID id) {
        branchFacade.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
