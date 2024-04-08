package com.makingscience.levelupproject.controller;

import com.makingscience.levelupproject.facade.routers.WaitingListFacadeRouter;
import com.makingscience.levelupproject.model.WaitingListDTO;
import com.makingscience.levelupproject.model.params.WaitingListRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/waitingList")
@RequiredArgsConstructor
@Validated
@Slf4j
public class WaitingListController {
        private final WaitingListFacadeRouter waitingListFacadeRouter;
    @PostMapping
    public ResponseEntity<WaitingListDTO> add(@Valid @RequestBody WaitingListRequest param) {
        WaitingListDTO waitingListDTO = waitingListFacadeRouter.add(param);
        return ResponseEntity.ok(waitingListDTO);
    }
}
