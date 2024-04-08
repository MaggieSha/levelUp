package com.makingscience.levelupproject.utils;

import com.makingscience.levelupproject.facade.interfaces.WaitingListFacade;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.entities.postgre.WaitingList;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
import com.makingscience.levelupproject.service.BranchService;
import com.makingscience.levelupproject.service.WaitingListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class Scheduler {
    private final List<WaitingListFacade> waitingListFacades;
    private final BranchService branchService;
    private final WaitingListService waitingListService;

    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 1000)
    public void updateWaitingList() {
        log.info("The time is now {}", LocalDateTime.now(ZoneId.of("Asia/Tbilisi")));
        List<WaitingList> waitingLists = waitingListService.getByStatus(WaitingStatus.ACTIVE);

        for(WaitingList waitingList : waitingLists){
            chooseFacade(waitingList.getBranch().getId()).updateWaitingList(waitingList.getId());

        }

    }

    private WaitingListFacade chooseFacade(UUID branchId) {
        Branch branch = branchService.getById(branchId);
        return waitingListFacades.stream().filter(waitingListFacade -> waitingListFacade.getType().name().equals(branch.getMerchant().getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not supported category type: " + branch.getMerchant().getCategory().getName()));
    }

}
