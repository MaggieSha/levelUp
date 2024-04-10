package com.makingscience.levelupproject.facade.restaurant;

import com.makingscience.levelupproject.facade.interfaces.SlotFacade;
import com.makingscience.levelupproject.model.dto.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.*;
import com.makingscience.levelupproject.repository.FilterQueryResponse;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.utils.JsonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantSlotFacade implements SlotFacade {
    private final BranchService branchService;
    private final SlotService slotService;
    private final JsonUtils jsonUtils;

    @Override
    public SlotDTO createSlot(CreateSlotParam param) {
        Branch branch = branchService.getById(param.getBranchId());

        RestaurantSlotDetails restaurantSlotDetails = (RestaurantSlotDetails) param.getSlotDetails();

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
        slot.setSlotDetails(restaurantSlotDetails);
        slot = slotService.save(slot);
        return SlotDTO.of(slot, restaurantSlotDetails);
    }







    @Override
    public SlotDTO updateSlot(Slot slot, UpdateSlotParam param) {

        if (param.getExternalId() != null) slot.setExternalId(param.getExternalId());
        if (param.getName() != null) slot.setName(param.getName());
        if (param.getReserveFee() != null) slot.setReserveFee(param.getReserveFee());


        RestaurantSlotDetails oldSlotDetails = (RestaurantSlotDetails) slot.getSlotDetails();

        if (param.getSlotDetails() != null) {
            RestaurantSlotDetails newSlotDetails = (RestaurantSlotDetails) param.getSlotDetails();

            if (newSlotDetails.getTableCapacity() != null)
                oldSlotDetails.setTableCapacity(newSlotDetails.getTableCapacity());
            if (newSlotDetails.getReservationStartTime() != null)
                oldSlotDetails.setReservationStartTime(newSlotDetails.getReservationStartTime());
            if (newSlotDetails.getReservationEndTime() != null)
                oldSlotDetails.setReservationEndTime(newSlotDetails.getReservationEndTime());
            if (newSlotDetails.getPaidCancelledHours() != null)
                oldSlotDetails.setPaidCancelledHours(newSlotDetails.getPaidCancelledHours());


            slot.setSlotDetails(oldSlotDetails);

        }

        slot = slotService.save(slot);
        return SlotDTO.of(slot, oldSlotDetails);

    }



    @Override
    public Type getType() {
        return Type.RESTAURANT;
    }


    @Override
    public RestaurantSlotDetails getDetails(String slotDetails) {
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
    public Page<FilterQueryResponse> filter(SlotFilterParam param, Pageable pageable) {
        RestaurantSlotFilterDetails filter = (RestaurantSlotFilterDetails) param.getSlotFilterDetails();
        validateParam(filter);

        Page<FilterQueryResponse> slots = slotService.filterForRestaurant(filter.getNumberOfPeople(), filter.getPreferredDay(), param.getBranchId(), pageable);

        return slots;


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
}
