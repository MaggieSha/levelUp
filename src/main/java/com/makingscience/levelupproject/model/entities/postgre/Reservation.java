package com.makingscience.levelupproject.model.entities.postgre;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.makingscience.levelupproject.model.details.reservation.ReservationDetails;
import com.makingscience.levelupproject.model.details.reservation.RestaurantReservationDetails;
import com.makingscience.levelupproject.model.enums.ReservationStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "reservation_day")
    private LocalDate reservationDay;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "reservation_time")
    private LocalTime reservationTime;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Type(JsonType.class)
    @Column(name = "details",columnDefinition = "jsonb")
    private ReservationDetails reservationDetails;
}
