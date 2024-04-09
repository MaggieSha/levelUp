package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Review,Long> {


    @Query("select avg(r.rating) from Review  r where r.branch.id = :branchId")
    BigDecimal countAverageForBranch(UUID branchId);

    @Query("select avg(r.rating) from Review  r where r.branch.merchant.id = :merchantId")
    BigDecimal countAverageForMerchant(UUID merchantId);

    @Query("select r from Review  r where r.reservation.id = :id")
    Optional<Review> findByReservationId(Long id);
}
