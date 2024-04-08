package com.makingscience.levelupproject.service;

import com.makingscience.levelupproject.model.entities.postgre.WaitingList;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
import com.makingscience.levelupproject.repository.WaitingListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingListService {

    private final WaitingListRepository waitingListRepository;

    public WaitingList save(WaitingList waitingList) {
        return waitingListRepository.save(waitingList);
    }

    public List<WaitingList> getByStatus(WaitingStatus waitingStatus) {
        return waitingListRepository.getByWaitingStatus(waitingStatus);
    }

    public WaitingList getById(Long id) {
        return waitingListRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Waiting list with id " + id + " not found!"));

    }

    public WaitingList getByIdAndStatus(Long id,WaitingStatus status) {
        return waitingListRepository.findByIdAndWaitingStatus(id,status).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Waiting list with id " + id + " not found!"));
    }
}
