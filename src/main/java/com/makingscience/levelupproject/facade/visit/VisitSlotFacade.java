package com.makingscience.levelupproject.facade.visit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.makingscience.levelupproject.facade.interfaces.SlotFacade;
import com.makingscience.levelupproject.model.dto.SlotDTO;
import com.makingscience.levelupproject.model.details.slot.VisitSlotDetails;
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
public class VisitSlotFacade implements SlotFacade {
    private final BranchService branchService;
    private final SlotService slotService;
    private final JsonUtils jsonUtils;

    @Override
    public SlotDTO createSlot(CreateSlotParam param) throws JsonProcessingException {
        Branch branch = branchService.getById(param.getBranchId());

        VisitSlotDetails slotDetails = (VisitSlotDetails) param.getSlotDetails();

        validateParam(slotDetails);

        Optional<Slot> optional = slotService.findByExternalIdAndBranchIdAndDetails(param.getExternalId(), param.getBranchId(),slotDetails);
        if (optional.isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot with same details already exists!");

        Slot slot = new Slot();
        slot.setSlotStatus(SlotStatus.ACTIVE);
        slot.setBranch(branch);
        slot.setExternalId(param.getExternalId());
        slot.setReserveFee(param.getReserveFee());
        slot.setName(param.getName());

        slot.setSlotDetails(slotDetails);
        slot = slotService.save(slot);
        return SlotDTO.of(slot, slotDetails);
    }


    @Override
    public SlotDTO updateSlot(Slot slot, UpdateSlotParam param) {
        if (param.getExternalId() != null) slot.setExternalId(param.getExternalId());
        if (param.getName() != null) slot.setName(param.getName());
        if (param.getReserveFee() != null) slot.setReserveFee(param.getReserveFee());

        VisitSlotDetails oldSlotDetails = (VisitSlotDetails) slot.getSlotDetails();


        if (param.getSlotDetails() != null) {
            VisitSlotDetails newSlotDetails = (VisitSlotDetails) param.getSlotDetails();

            if (newSlotDetails.getVisitHour() != null)
                oldSlotDetails.setVisitHour(newSlotDetails.getVisitHour());
            if (newSlotDetails.getServiceName() != null)
                oldSlotDetails.setServiceName(newSlotDetails.getServiceName());


            slot.setSlotDetails(oldSlotDetails);
        }

        slot = slotService.save(slot);
        return SlotDTO.of(slot, oldSlotDetails);

    }




    @Override
    public Type getType() {
        return Type.VISIT;
    }


    @Override
    public VisitSlotDetails getDetails(String slotDetails) {
        VisitSlotDetails details;
        try {
            details = jsonUtils.deserialize(slotDetails, VisitSlotDetails.class);
        } catch (Exception e) {
            log.error("Can not deserialize visit slot details!");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Can not deserialize visit slot details!");
        }
        return details;
    }

    @Override
    public Page<FilterQueryResponse> filter(SlotFilterParam param, Pageable pageable) {
        Branch branch = branchService.getById(param.getBranchId());
        VisitSlotFilterDetails filter = getVisitSlotFilterDetails(param.getSlotFilterDetails(), branch);
        validateParam(filter);

        Page<FilterQueryResponse> slots = slotService.filterForVisit(filter.getServiceName(), filter.getPreferredTime(), filter.getPreferredDay(), param.getBranchId(), pageable);

        return slots;


    }

    private static VisitSlotFilterDetails getVisitSlotFilterDetails(SlotFilterDetails param, Branch branch) {
        VisitSlotFilterDetails visitSlotFilterDetails;
        if (param instanceof VisitSlotFilterDetails) {
            visitSlotFilterDetails = (VisitSlotFilterDetails) param;
        } else
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot type should be " + branch.getMerchant().getCategory().getName());
        return visitSlotFilterDetails;
    }

    private static void validateParam(VisitSlotFilterDetails visitSlotFilterDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<VisitSlotFilterDetails>> violations = validator.validate(visitSlotFilterDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<VisitSlotFilterDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }



    private static void validateParam(VisitSlotDetails slotDetails) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<VisitSlotDetails>> violations = validator.validate(slotDetails);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<VisitSlotDetails> v : violations) {
                errorMessage.append(v.getMessage()).append("; ");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage.toString());

        }
    }
}
