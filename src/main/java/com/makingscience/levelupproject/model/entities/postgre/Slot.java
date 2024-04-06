package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.Type;
import com.makingscience.levelupproject.model.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@jakarta.persistence.Table(name = "slot")
@RequiredArgsConstructor
public class Slot {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "external_id")
    private String externalId;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "reserve_fee")
    private Double reserveFee;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;


    @OneToMany(mappedBy = "slot")
    private Set<Reservation> reservationSet;

    @Column(name = "details")
    private String slotDetails;



}
