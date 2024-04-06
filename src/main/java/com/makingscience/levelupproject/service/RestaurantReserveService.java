package com.makingscience.levelupproject.service;


import com.makingscience.levelupproject.model.ReservationDTO;
import com.makingscience.levelupproject.model.details.request.RestaurantReservationRequestDetails;
import com.makingscience.levelupproject.model.details.reservation.RestaurantReservationDetails;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.entities.postgre.User;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.ReservationRequest;
import com.makingscience.levelupproject.repository.ReservationRepository;
import com.makingscience.levelupproject.repository.SlotRepository;
import com.makingscience.levelupproject.service.interfaces.ReservationService;
import com.makingscience.levelupproject.utils.JsonUtils;
import com.makingscience.levelupproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantReserveService implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final JwtUtils jwtUtils;
    private final JsonUtils jsonUtils;


    @Override
    public ReservationDTO reserve(ReservationRequest param)  {
        User authenticatedUser = jwtUtils.getAuthenticatedUser();
        RestaurantReservationRequestDetails requestDetails = (RestaurantReservationRequestDetails)param.getReservationRequestDetails();


        Slot slot = slotRepository.findByIdAndLock(param.getSlotId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found by id " + param.getSlotId()));
        RestaurantSlotDetails slotDetails = null;
        try {
             slotDetails= jsonUtils.deserialize(slot.getSlotDetails(), RestaurantSlotDetails.class);
        } catch (Exception e) {
           log.error("Can not deserialize restaurant slot details!");

        }


        if(param.getReservationTime().isBefore(slotDetails.getReservationStartTime()) ||
        param.getReservationTime().isAfter(slotDetails.getReservationEndTime())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Reservation is between " + slotDetails.getReservationStartTime() + " and " + slotDetails.getReservationEndTime() + " hours.");
        }

        if(requestDetails.getNumberOfPeople() > slotDetails.getTableCapacity()){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Slot max capacity is  " + slotDetails.getTableCapacity());
        }

        // mgoni ak sheidzleba dz bevri wamoigos amitom query jobs romelic wamoigebs statuseebis da droebis mixedvit
        Set<Reservation> reservations = slot.getReservationSet();
        for(Reservation reservation : reservations) {
            if((reservation.getReservationStatus().equals(ReservationStatus.PENDING) || reservation.getReservationStatus().equals(ReservationStatus.CONFIRMED) ||
                    reservation.getReservationStatus().equals(ReservationStatus.CHECKED_IN))
            && reservation.getReservationDay().equals(param.getReservationDay())){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Slot is already reserved!");
            }
        }
        Reservation reservation = new Reservation();
        reservation.setReservationDay(param.getReservationDay());
        reservation.setReservationTime(param.getReservationTime());
        reservation.setReservationStatus(ReservationStatus.PENDING);
        // TODO:rodesac gadaixdis mashin sheicvleba damodi yovel 10 wutshi vamowmeb, tu pending aris,mashin vucvli cancelledze
        reservation.setSlot(slot);
        reservation.setUser(authenticatedUser);

        RestaurantReservationDetails reservationDetails = new RestaurantReservationDetails();
        reservationDetails.setNumberOfPeople(requestDetails.getNumberOfPeople());


        try{
            String details = jsonUtils.serialize(reservationDetails);
            reservation.setReservationDetails(details);
        }catch (Exception e){
            log.error("Error during reservation details serialization - {}!",e);
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Error during reservation details serialization - " + e + "!");
        }
        reservationRepository.save(reservation);

        // payment unda gaaketo
        // ak daubrundeba reservation id da tanxa da unda movides gadaxdaze. tu gadaixdis mashsi nstatusi sheicvleba
        return ReservationDTO.of(reservation);
    }

    @Override
    public Type getType() {
        return Type.RESTAURANT;
    }
}
