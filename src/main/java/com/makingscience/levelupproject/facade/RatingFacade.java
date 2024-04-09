package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.entities.postgre.Review;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.params.RatingParam;
import com.makingscience.levelupproject.service.RatingService;
import com.makingscience.levelupproject.service.ReservationService;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RatingFacade {
    private final RatingService ratingService;
    private final JwtUtils jwtUtils;
    private final ReservationService reservationService;
    public static final ZoneId ZONE_ID = ZoneId.of("Asia/Tbilisi");

    public void add(RatingParam param) {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        Reservation reservation = checkReservationAndReviewExistence(param, authenticatedUser);
        Review review = new Review();
        review.setRating(param.getRating());
        review.setRatingTime(LocalDateTime.now(ZONE_ID));
        review.setUser(authenticatedUser);
        review.setComment(param.getComment());
        review.setBranch(reservation.getSlot().getBranch());
        review.setReservation(reservation);
        ratingService.save(review);
    }

    private Reservation checkReservationAndReviewExistence(RatingParam param, User authenticatedUser) {
        Reservation reservation = reservationService.getByIdAndUser(param.getReservationId(), authenticatedUser.getId());
        if(!List.of(ReservationStatus.CHECKED_IN,ReservationStatus.COMPLETED).contains(reservation.getReservationStatus())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can not review reservation with status other thant COMPLETED,CHECKED_IN");

        }
        Optional<Review> review = ratingService.findByReservationId(reservation.getId());
        if(review.isPresent()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User already made a review with reservation id: " + param.getReservationId());

        }
        return reservation;


    }

    public BigDecimal getMerchantRating(UUID merchantId) {
        return ratingService.countAverageForMerchant(merchantId);
    }

    public BigDecimal getBranchRating(UUID branchId) {
        return ratingService.countAverageForBranch(branchId);
    }
}
