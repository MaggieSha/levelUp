package com.makingscience.levelupproject.facade.routers;

import com.makingscience.levelupproject.facade.interfaces.WaitingListFacade;
import com.makingscience.levelupproject.model.WaitingListDTO;
import com.makingscience.levelupproject.model.entities.postgre.Branch;
import com.makingscience.levelupproject.model.params.WaitingListRequest;
import com.makingscience.levelupproject.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WaitingListFacadeRouter {
    private final List<WaitingListFacade> waitingListFacades;
    private final BranchService branchService;
    public WaitingListDTO add(WaitingListRequest param) {
        return chooseFacade(param.getBranchId()).add(param);
    }

    private WaitingListFacade chooseFacade(UUID branchId) {
        Branch branch = branchService.getById(branchId);
        return waitingListFacades.stream().filter(waitingListFacade -> waitingListFacade.getType().name().equals(branch.getMerchant().getCategory().getName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not supported category type: " + branch.getMerchant().getCategory().getName()));
    }
}
