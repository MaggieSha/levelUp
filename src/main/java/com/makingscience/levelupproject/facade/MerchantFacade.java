package com.makingscience.levelupproject.facade;

import com.makingscience.levelupproject.model.MerchantDTO;
import com.makingscience.levelupproject.model.entities.postgre.*;
import com.makingscience.levelupproject.model.enums.*;
import com.makingscience.levelupproject.model.params.CreateMerchantParam;
import com.makingscience.levelupproject.model.params.UpdateMerchantParam;
import com.makingscience.levelupproject.service.*;
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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MerchantFacade {
    private final MerchantService merchantService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BranchService branchService;
    private final SlotService slotService;

    public MerchantDTO add(CreateMerchantParam param) {
        Optional<Merchant> optionalMerchant = merchantService.getByEmail(param.getEmail());
        Optional<User> optionalUser = userService.getByEmail(param.getEmail());
        if (optionalUser.isPresent() || optionalMerchant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User or Merchant already exists with email: " + param.getEmail());
        }

        optionalMerchant = merchantService.getByPhone(param.getContactPhone());
        optionalUser = userService.getByContactPhone(param.getContactPhone());
        if (optionalMerchant.isPresent() || optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User or Merchant already exists with contact phone: " + param.getContactPhone());
        }

        Category category = categoryService.findById(param.getCategoryId());
        Merchant merchant = new Merchant();
        merchant.setEmail(param.getEmail());
        merchant.setCategory(category);
        merchant.setName(param.getName());
        merchant.setPhone(param.getContactPhone());
        merchant.setIban(param.getIban());
        merchant.setIdentificationNumber(param.getIdentificationNumber());
        merchant.setImage(param.getImageAddress());
        merchant.setDocumentAddress(param.getDocumentAddress());
        merchant.setStatus(MerchantStatus.ACTIVE);
        merchant = merchantService.save(merchant);
       return MerchantDTO.of(merchant);

    }

    public MerchantDTO update(UpdateMerchantParam param) {
        Merchant merchant = merchantService.getById(param.getId());

        if (param.getEmail() != null && !param.getEmail().equals(merchant.getEmail())) {
            Optional<Merchant> optionalMerchant = merchantService.getByEmail(param.getEmail());
            Optional<User> optionalUser = userService.getByEmail(param.getEmail());
            if (optionalUser.isPresent() || optionalMerchant.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User or Merchant already exists with email: " + param.getEmail());
            }
            merchant.setEmail(param.getEmail());
        }
        if (param.getContactPhone() != null && !param.getContactPhone().equals(merchant.getPhone())) {
            Optional<Merchant> optionalMerchant = merchantService.getByPhone(param.getContactPhone());
            Optional<User> optionalUser = userService.getByContactPhone(param.getContactPhone());
            if (optionalMerchant.isPresent() || optionalUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User or Merchant already exists with contact phone: " + param.getContactPhone());
            }
            merchant.setPhone(param.getContactPhone());
        }


        if (param.getName() != null) merchant.setName(param.getName());
        if (param.getIban() != null) merchant.setIban(param.getIban());
        if (param.getIdentificationNumber() != null) merchant.setIdentificationNumber(param.getIdentificationNumber());
        if (param.getImageAddress() != null) merchant.setImage(param.getImageAddress());
        if (param.getDocumentAddress() != null) merchant.setDocumentAddress(param.getDocumentAddress());

        merchant = merchantService.save(merchant);
        return MerchantDTO.of(merchant);
    }

    @Transactional
    public void delete(UUID id) {
        Merchant merchant = merchantService.getById(id);
        merchant.setStatus(MerchantStatus.DELETED);

        Set<Branch> branchSet = merchant.getBranchSet();
        for (Branch branch : branchSet) {
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

        merchantService.save(merchant);

    }

    public Page<MerchantDTO> getAllMerchants(Pageable pageable) {
        Page<Merchant> merchants = merchantService.getAllMerchants(pageable);
        List<MerchantDTO> dtos  = merchants.stream().map(MerchantDTO::toShortDTO).toList();
        return new PageImpl<>(dtos,pageable,merchants.getTotalElements());
    }

    public MerchantDTO getById(UUID id) {
        return MerchantDTO.of(merchantService.getById(id));
    }

    public Page<MerchantDTO> getAllMerchantsByCategory(Pageable pageable, Long categoryId) {
        Page<Merchant> merchants = merchantService.getAllMerchantsByCategory(pageable,categoryId);
        List<MerchantDTO> dtos  = merchants.stream().map(MerchantDTO::toShortDTO).toList();
        return new PageImpl<>(dtos,pageable,merchants.getTotalElements());
    }

    public Page<MerchantDTO> getAllMerchantsByRating(Pageable pageable) {
        return  merchantService.getAllMerchantByRating(pageable);
    }
}
