package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.RestaurantSlotDetails;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.Slot;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.params.CreateSlotParam;
import com.makingscience.levelupproject.model.params.UpdateSlotParam;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.utils.JsonUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public SlotDTO add(CreateSlotParam param) {
        RestaurantSlotDetails restaurantSlotDetails;
        Branch branch = branchService.getById(param.getBranchId());
        if (param.getSlotDetails() instanceof RestaurantSlotDetails) {
            restaurantSlotDetails = (RestaurantSlotDetails) param.getSlotDetails();
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
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
        Optional<Slot> optional = slotService.findByExternalIdAndBranchId(param.getExternalId(), param.getBranchId());
        if (optional.isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Slot with external id " + param.getExternalId() + " and branch id " + param.getBranchId() + " already exists!");
        Slot slot = new Slot();
        slot.setSlotStatus(SlotStatus.ACTIVE);
        slot.setBranch(branch);
        slot.setExternalId(param.getExternalId());
        slot.setReserveFee(param.getReserveFee());
        slot.setName(param.getName());

        try {
            String details = jsonUtils.serialize(restaurantSlotDetails);
            slot.setSlotDetails(details);
        } catch (Exception e) {
            log.error("Error during slot details serialization - {}!", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error during slot details serialization - " + e + "!");
        }
        slot = slotService.save(slot);
        return SlotDTO.of(slot, restaurantSlotDetails);
    }

    @Override
    public SlotDTO update(Slot slot, UpdateSlotParam param) {
        if (param.getExternalId() != null) slot.setExternalId(param.getExternalId());
        if (param.getName() != null) slot.setName(param.getName());
        if (param.getReserveFee() != null) slot.setReserveFee(param.getReserveFee());
        RestaurantSlotDetails oldSlotDetails = getDetails(slot.getSlotDetails());
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


            try {
                String details = jsonUtils.serialize(oldSlotDetails);
                slot.setSlotDetails(details);
            } catch (Exception e) {
                log.error("Error during slot details serialization - {}!", e);
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Error during slot details serialization - " + e + "!");
            }


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
}
