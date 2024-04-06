package com.makingscience.levelupproject.model.entities.postgre;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_rating")
@RequiredArgsConstructor
public class UserRating {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id" , referencedColumnName = "id")
    private Branch branch;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "rating_time")
    private LocalDateTime ratingTime;

    @Column(name = "comment")
    private String comment;





}
