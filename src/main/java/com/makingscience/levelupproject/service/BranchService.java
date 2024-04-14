package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.enums.BranchStatus;
import com.makingscience.levelupproject.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public Optional<Branch> getByContactPhone(String contactPhone) {
        return branchRepository.getByContactPhone(contactPhone);
    }

    public Branch save(Branch branch) {
        return branchRepository.save(branch);
    }
    public Branch getById(UUID id) {
        return branchRepository.findByIdAndStatus(id,BranchStatus.ACTIVE).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Branch with id " + id + " not found!"));
    }

    public Optional<Branch> getByIdAndStatus(UUID id, BranchStatus branchStatus) {
        return branchRepository.findByIdAndStatus(id,branchStatus);
    }

    public Page<Branch> getAllByMerchantId(Pageable pageable, UUID merchantId) {
        return branchRepository.getAllByMerchantId(pageable,merchantId);
    }
}
