package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.facade.interfaces.SlotFacade;
import com.makingscience.levelupproject.model.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Reservation;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.*;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.ReservationService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.utils.JsonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantSlotFacade implements SlotFacade {
    private final BranchService branchService;
    private final SlotService slotService;
    private final JsonUtils jsonUtils;
    private final ReservationService reservationService;

    @Override
    public SlotDTO add(CreateSlotParam param) {
        Branch branch = branchService.getById(param.getBranchId());
        RestaurantSlotDetails restaurantSlotDetails = getRestaurantSlotDetails(param.getSlotDetails(), branch);

        validateParam(restaurantSlotDetails);

        Optional<Slot> optional = slotService.findByExternalIdAndBranchId(param.getExternalId(), param.getBranchId());
        if (optional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot with external id " + param.getExternalId() + " and branch id " + param.getBranchId() + " already exists!");

        Slot slot = new Slot();
        slot.setSlotStatus(SlotStatus.ACTIVE);
        slot.setBranch(branch);
        slot.setExternalId(param.getExternalId());
        slot.setReserveFee(param.getReserveFee());
        slot.setName(param.getName());

        setDetails(restaurantSlotDetails, slot);
        slot = slotService.save(slot);
        return SlotDTO.of(slot, restaurantSlotDetails);
    }

    private void setDetails(RestaurantSlotDetails restaurantSlotDetails, Slot slot) {
        try {
            String details = jsonUtils.serialize(restaurantSlotDetails);
            slot.setSlotDetails(details);
        } catch (Exception e) {
            log.error("Error during slot details serialization - {}!", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error during slot details serialization - " + e + "!");
        }
    }

    private static void validateParam(RestaurantSlotDetails restaurantSlotDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<RestaurantSlotDetails>> violations = validator.validate(restaurantSlotDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<RestaurantSlotDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }


    private static RestaurantSlotDetails getRestaurantSlotDetails(SlotDetails param, Branch branch) {
        RestaurantSlotDetails restaurantSlotDetails;
        if (param instanceof RestaurantSlotDetails) {
            restaurantSlotDetails = (RestaurantSlotDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
        return restaurantSlotDetails;
    }

    @Override
    public SlotDTO update(Slot slot, UpdateSlotParam param) {
        if (param.getExternalId() != null) slot.setExternalId(param.getExternalId());
        if (param.getName() != null) slot.setName(param.getName());
        if (param.getReserveFee() != null) slot.setReserveFee(param.getReserveFee());
        RestaurantSlotDetails oldSlotDetails = toDetails(slot.getSlotDetails());
        if (param.getSlotDetails() != null) {
            RestaurantSlotDetails newSlotDetails;
            if (param.getSlotDetails() instanceof RestaurantSlotDetails) {
                newSlotDetails = (RestaurantSlotDetails) param.getSlotDetails();
            } else
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + slot.getBranch().getMerchant().getCategory().getName());

            if (newSlotDetails.getTableCapacity() != null)
                oldSlotDetails.setTableCapacity(newSlotDetails.getTableCapacity());
            if (newSlotDetails.getReservationStartTime() != null)
                oldSlotDetails.setReservationStartTime(newSlotDetails.getReservationStartTime());
            if (newSlotDetails.getReservationEndTime() != null)
                oldSlotDetails.setReservationEndTime(newSlotDetails.getReservationEndTime());
            if (newSlotDetails.getPaidCancelledHours() != null)
                oldSlotDetails.setPaidCancelledHours(newSlotDetails.getPaidCancelledHours());


            setDetails(oldSlotDetails, slot);


        }

        slot = slotService.save(slot);
        return SlotDTO.of(slot, oldSlotDetails);

    }


    @Override
    public Type getType() {
        return Type.RESTAURANT;
    }

    @Override
    public SlotDTO getById(Slot slot) {
        RestaurantSlotDetails slotDetails = null;
        try {
            slotDetails = jsonUtils.deserialize(slot.getSlotDetails(), RestaurantSlotDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize restaurant slot details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize restaurant slot details!");
        }
        return SlotDTO.of(slot, slotDetails);
    }


    @Override
    public RestaurantSlotDetails toDetails(String slotDetails) {
        RestaurantSlotDetails details;
        try {
            details = jsonUtils.deserialize(slotDetails, RestaurantSlotDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize restaurant slot details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize restaurant slot details!");
        }
        return details;
    }

    @Override
    public Page<Slot> filter(SlotFilterParam param, Pageable pageable) {
        Branch branch = branchService.getById(param.getBranchId());
        RestaurantSlotFilterDetails filter = getRestaurantSlotFilterDetails(param.getSlotFilterDetails(), branch);
        validateParam(filter);

        Set<Slot> allSlots = branch.getSlotSet();
        List<Slot> availableSlots = new ArrayList<>();


        for (Slot slot : allSlots) {
            RestaurantSlotDetails details = toDetails(slot.getSlotDetails());
            if (details.getTableCapacity() < filter.getNumberOfPeople()) continue;
            List<Reservation> reservations = reservationService.getByStatusAndReservationDay(List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN), filter.getPreferredDay());
            if(!reservations.isEmpty()) continue;
            availableSlots.add(slot);
        }

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        int startIndex = pageNumber * pageSize;

        if (startIndex < availableSlots.size()) {
            int endIndex = Math.min(startIndex + pageSize, availableSlots.size());

            List<Slot> pageRecords = availableSlots.subList(startIndex, endIndex);
            return new PageImpl<>(pageRecords, pageable, availableSlots.size());


        } else return new PageImpl<>(new ArrayList<>(), pageable, availableSlots.size());

    }

    private static RestaurantSlotFilterDetails getRestaurantSlotFilterDetails(SlotFilterDetails param, Branch branch) {
        RestaurantSlotFilterDetails restaurantSlotFilterDetails;
        if (param instanceof RestaurantSlotFilterDetails) {
            restaurantSlotFilterDetails = (RestaurantSlotFilterDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
        return restaurantSlotFilterDetails;
    }

    private static void validateParam(RestaurantSlotFilterDetails restaurantSlotDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<RestaurantSlotFilterDetails>> violations = validator.validate(restaurantSlotDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<RestaurantSlotFilterDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }
}
