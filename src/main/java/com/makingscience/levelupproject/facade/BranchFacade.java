package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.dto.BranchDTO;
import com.makingscience.levelupproject.model.entities.postgre.*;
import com.makingscience.levelupproject.model.enums.*;
import com.makingscience.levelupproject.model.params.CreateBranchParam;
import com.makingscience.levelupproject.model.params.UpdateBranchParam;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.MerchantService;
import com.makingscience.levelupproject.service.SlotService;
import com.makingscience.levelupproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BranchFacade {

    private final BranchService branchService;
    private final MerchantService merchantService;
    private final UserService userService;
    private final SlotService slotService;


    @Transactional
    public void delete(UUID id) {
        Branch branch = branchService.getByIdAndStatus(id, BranchStatus.ACTIVE).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found with id " + id));

        branch.setStatus(BranchStatus.DELETED);
        branchService.save(branch);
        Set<Slot> slotSet = branch.getSlotSet();
        for (Slot slot : slotSet) {
            slot.setSlotStatus(SlotStatus.DELETED);
            slotService.save(slot);
            Set<Reservation> reservationSet = slot.getReservationSet();
            for (Reservation reservation : reservationSet) {
                if (reservation.getReservationStatus().equals(ReservationStatus.PENDING)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                }
            }
        }
        Set<WaitingList> waitingListSet = branch.getWaitingListSet();
        for (WaitingList waitingList : waitingListSet) {
            if (waitingList.getWaitingStatus().equals(WaitingStatus.ACTIVE)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        }


    }

    public BranchDTO update(UpdateBranchParam param) {
        Branch branch = branchService.getByIdAndStatus(param.getId(), BranchStatus.ACTIVE).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found with id " + param.getId()));

        if (!param.getContactPhone().equals(branch.getContactPhone())) {
            Optional<Merchant> optionalMerchant = merchantService.getByPhone(param.getContactPhone());
            Optional<Branch> optionalBranch = branchService.getByContactPhoneAndMerchantId(param.getContactPhone(), branch.getMerchant().getId());
            Optional<User> optionalUser = userService.getByContactPhone(param.getContactPhone());
            if (optionalMerchant.isPresent() || optionalUser.isPresent() || optionalBranch.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User,Merchant or Branch(of another merchant) already exists with contact phone: " + param.getContactPhone());
            }
            branch.setContactPhone(param.getContactPhone());

        }
        if (param.getName() != null) branch.setName(param.getName());
        if (param.getReserveFee() != null) branch.setReserveFee(param.getReserveFee());
        if (param.getIban() != null) branch.setIban(param.getIban());

        if (param.getAddress() != null) branch.setAddress(param.getAddress());
        if (param.getImageAddress() != null) branch.setImage(param.getImageAddress());
        Branch save = branchService.save(branch);

        return BranchDTO.of(save);
    }

    public BranchDTO add(CreateBranchParam param) {

        Merchant merchant = merchantService.getById(param.getMerchantId());

        if (!param.getContactPhone().equals(merchant.getPhone())) {
            Optional<Merchant> optionalMerchant = merchantService.getByPhone(param.getContactPhone());
            Optional<Branch> optionalBranch = branchService.getByContactPhoneAndMerchantId(param.getContactPhone(), param.getMerchantId());
            Optional<User> optionalUser = userService.getByContactPhone(param.getContactPhone());
            if (optionalMerchant.isPresent() || optionalUser.isPresent() || optionalBranch.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User,Merchant or Branch(of another merchant) already exists with contact phone: " + param.getContactPhone());
            }

        }
        Branch branch = new Branch();
        branch.setContactPhone(param.getContactPhone());
        branch.setMerchant(merchant);
        branch.setName(param.getName());
        branch.setReserveFee(param.getReserveFee());
        if (branch.getIban() != null) branch.setIban(param.getIban());
        else branch.setIban(merchant.getIban());
        branch.setAddress(param.getAddress());
        branch.setImage(param.getImageAddress());
        branch.setStatus(BranchStatus.ACTIVE);
        Branch save = branchService.save(branch);

        return BranchDTO.of(save);
    }

    public BranchDTO getById(UUID id) {
        return BranchDTO.of(branchService.getById(id));
    }

    public Page<BranchDTO> getAllByMerchantId(Pageable pageable, UUID merchantId) {
        Page<Branch> branches = branchService.getAllByMerchantId(pageable,merchantId);
        List<BranchDTO> dtos  = branches.stream().map(BranchDTO::of).toList();
        return new PageImpl<>(dtos,pageable,branches.getTotalElements());
    }
}
