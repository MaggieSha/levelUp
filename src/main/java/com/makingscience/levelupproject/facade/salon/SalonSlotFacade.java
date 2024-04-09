package com.makingscience.levelupproject.facade.salon;

import com.makingscience.levelupproject.facade.interfaces.SlotFacade;
import com.makingscience.levelupproject.model.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.SalonSlotDetails;
import com.makingscience.levelupproject.model.details.slot.SlotDetails;
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
import org.jetbrains.annotations.NotNull;
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
public class SalonSlotFacade implements SlotFacade {
    private final BranchService branchService;
    private final SlotService slotService;
    private final JsonUtils jsonUtils;

    @Override
    public SlotDTO createSlot(CreateSlotParam param) {
        Branch branch = branchService.getById(param.getBranchId());
        SalonSlotDetails slotDetails = getSlotDetails(param.getSlotDetails(), branch);

        validateParam(slotDetails);

        Optional<Slot> optional = slotService.findByExternalIdAndBranchId(param.getExternalId(), param.getBranchId());
        if (optional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot with external id " + param.getExternalId() + " and branch id " + param.getBranchId() + " already exists!");

        Slot slot = new Slot();
        slot.setSlotStatus(SlotStatus.ACTIVE);
        slot.setBranch(branch);
        slot.setExternalId(param.getExternalId());
        slot.setReserveFee(param.getReserveFee());
        slot.setName(param.getName());

        setDetails(slotDetails, slot);
        slot = slotService.save(slot);
        return SlotDTO.of(slot, slotDetails);
    }

    private void setDetails(SalonSlotDetails slotDetails, Slot slot) {
        try {
            String details = jsonUtils.serialize(slotDetails);
            slot.setSlotDetails(details);
        } catch (Exception e) {
            log.error("Error during slot details serialization - {}!", e);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error during slot details serialization - " + e + "!");
        }
    }

    private static void validateParam(SalonSlotDetails slotDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<SalonSlotDetails>> violations = validator.validate(slotDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<SalonSlotDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }


    private static SalonSlotDetails getSlotDetails(SlotDetails param, Branch branch) {
        SalonSlotDetails slotDetails;
        if (param instanceof SalonSlotDetails) {
            slotDetails = (SalonSlotDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
        return slotDetails;
    }

    @Override
    public SlotDTO updateSlot(Slot slot, UpdateSlotParam param) {
        if (param.getExternalId() != null) slot.setExternalId(param.getExternalId());
        if (param.getName() != null) slot.setName(param.getName());
        if (param.getReserveFee() != null) slot.setReserveFee(param.getReserveFee());

        SalonSlotDetails oldSlotDetails = getDetails(slot.getSlotDetails());

        if (param.getSlotDetails() != null) {
            SalonSlotDetails newSlotDetails = getSlotDetails(slot, param);

            if (newSlotDetails.getStylistName() != null)
                oldSlotDetails.setStylistName(newSlotDetails.getStylistName());
            if (newSlotDetails.getVisitHour() != null)
                oldSlotDetails.setVisitHour(newSlotDetails.getVisitHour());
            if (newSlotDetails.getServiceName() != null)
                oldSlotDetails.setServiceName(newSlotDetails.getServiceName());


            setDetails(oldSlotDetails, slot);

        }

        slot = slotService.save(slot);
        return SlotDTO.of(slot, oldSlotDetails);

    }

    @NotNull
    private static SalonSlotDetails getSlotDetails(Slot slot, UpdateSlotParam param) {
        SalonSlotDetails newSlotDetails;
        if (param.getSlotDetails() instanceof SalonSlotDetails) {
            newSlotDetails = (SalonSlotDetails) param.getSlotDetails();
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + slot.getBranch().getMerchant().getCategory().getName());
        return newSlotDetails;
    }


    @Override
    public Type getType() {
        return Type.SALON;
    }


    @Override
    public SalonSlotDetails getDetails(String slotDetails) {
        SalonSlotDetails details;
        try {
            details = jsonUtils.deserialize(slotDetails, SalonSlotDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize salon slot details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize salon slot details!");
        }
        return details;
    }

    @Override
    public Page<FilterQueryResponse> filter(SlotFilterParam param, Pageable pageable) {
        Branch branch = branchService.getById(param.getBranchId());
        SalonSlotFilterDetails filter = getSalonSlotFilterDetails(param.getSlotFilterDetails(), branch);
        validateParam(filter);

        Page<FilterQueryResponse> slots = slotService.filterForSalon(filter.getServiceName(), filter.getStylistName(), filter.getPreferredTime().getHour(), filter.getPreferredDay(), param.getBranchId(), pageable);

        return slots;


    }

    private static SalonSlotFilterDetails getSalonSlotFilterDetails(SlotFilterDetails param, Branch branch) {
        SalonSlotFilterDetails salonSlotFilterDetails;
        if (param instanceof SalonSlotFilterDetails) {
            salonSlotFilterDetails = (SalonSlotFilterDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
        return salonSlotFilterDetails;
    }

    private static void validateParam(SalonSlotFilterDetails salonSlotFilterDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<SalonSlotFilterDetails>> violations = validator.validate(salonSlotFilterDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<SalonSlotFilterDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }
}
