package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.WaitingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@jakarta.persistence.Table(name = "waiting_list")
@RequiredArgsConstructor
public class WaitingList {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "preferred_date")
    private LocalDate preferredDate;

    @Column(name = "preferred_time")
    private LocalTime preferredTime;



    @Column(name = "waiting_status")
    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    private String waitingListDetails;

}
