package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.dto.MerchantDTO;
import com.makingscience.levelupproject.model.entities.postgre.Merchant;
import com.makingscience.levelupproject.model.enums.MerchantStatus;
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

    @Query("select new com.makingscience.levelupproject.model.MerchantDTO(m.id,m.name,m.category.id,m.category.name,m.email,m.phone,m.status,avg(r.rating) as rating) " +
            "from Merchant m left join Review r on m.id = r.branch.merchant.id group by m.id,m.name,m.category,m.email,m.phone,m.status order by rating ")
    Page<MerchantDTO> getAllMerchantByRating(Pageable pageable);
}
