package com.makingscience.levelupproject.model.entities.postgre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "branch_rating")
@RequiredArgsConstructor
public class Review {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "rating_time")
    private LocalDateTime ratingTime;

    @Column(name = "comment")
    private String comment;

    @OneToOne
    @JoinColumn(unique = true, name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;

}
