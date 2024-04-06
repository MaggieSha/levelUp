package com.makingscience.levelupproject.service;


import com.makingscience.levelupproject.model.entities.postgre.Merchant;
import com.makingscience.levelupproject.model.enums.MerchantStatus;
import com.makingscience.levelupproject.repository.MerchantRepository;
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
public class MerchantService {
    private final MerchantRepository merchantRepository;


    public Page<Merchant> getAllMerchants(Pageable pageable) {
        return merchantRepository.findAllByStatus(pageable,MerchantStatus.ACTIVE);
    }

    public Optional<Merchant> getByEmail(String email) {
        return merchantRepository.getByEmailAndStatus(email, MerchantStatus.ACTIVE);
    }

    public Optional<Merchant> getByPhone(String contactPhone) {
        return merchantRepository.getByPhoneAndStatus(contactPhone, MerchantStatus.ACTIVE);
    }

    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    public Merchant getById(UUID id) {
        return merchantRepository.getByIdAndStatus(id, MerchantStatus.ACTIVE).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Merchant with id " + id + " not found!"));
    }

    public Page<Merchant> getAllMerchantsByCategory(Pageable pageable, Long categoryId) {
        return merchantRepository.getAllMerchantsByCategory(pageable,categoryId,MerchantStatus.ACTIVE);
    }
}
