package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.ReservationStatus;
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
@jakarta.persistence.Table(name = "reservation")
@RequiredArgsConstructor
public class Reservation {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reservation_day")
    private LocalDate reservationDay;
    @Column(name = "reservation_time")
    private LocalTime reservationTime;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private String reservationDetails;
}
