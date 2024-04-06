package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Merchant;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.MerchantStatus;
import com.makingscience.levelupproject.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {


    Optional<Merchant> getByEmailAndStatus(String email,MerchantStatus status);

    Optional<Merchant> getByPhoneAndStatus(String contactPhone,MerchantStatus status);

    Optional<Merchant> findByIdAndStatus(UUID merchantId, MerchantStatus merchantStatus);

    Optional<Merchant> getByIdAndStatus(UUID id, MerchantStatus merchantStatus);

    @Query("select m from Merchant m where m.category.id = :categoryId and m.status = :status")
    Page<Merchant> getAllMerchantsByCategory(Pageable pageable, Long categoryId, MerchantStatus status);

    Page<Merchant> findAllByStatus(Pageable pageable,MerchantStatus status);
}
