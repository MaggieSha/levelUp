package com.makingscience.levelupproject.repository;

import com.makingscience.levelupproject.model.entities.postgre.WaitingList;
import com.makingscience.levelupproject.model.enums.WaitingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaitingListRepository extends JpaRepository<WaitingList,Long> {
    List<WaitingList> getByWaitingStatus(WaitingStatus waitingStatus);

    Optional<WaitingList> findByIdAndWaitingStatus(Long id, WaitingStatus status);
}
