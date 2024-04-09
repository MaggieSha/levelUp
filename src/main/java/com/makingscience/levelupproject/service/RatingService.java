package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.Review;
import com.makingscience.levelupproject.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public Optional<Review> findByReservationId(Long id) {

        return ratingRepository.findByReservationId(id);
    }

    public Review save(Review review) {
        return ratingRepository.save(review);
    }

    public BigDecimal countAverageForBranch(UUID branchId) {
        return ratingRepository.countAverageForBranch(branchId);
    }

    public BigDecimal countAverageForMerchant(UUID merchantId) {
        return ratingRepository.countAverageForMerchant(merchantId);
    }
}
