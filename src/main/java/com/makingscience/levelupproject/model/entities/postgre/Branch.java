package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.BranchStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "branch")
@RequiredArgsConstructor
public class Branch {
    @Id
    private UUID id;

    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Tbilisi"));

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;


    @Column(name = "contact_phone")
    private String contactPhone;


    @Column(name = "rating")
    private Double rating;


    @Column(name = "name")
    private String name;


    @Column(name = "reserve_fee")
    private Double reserveFee;

    @Column(name = "iban")
    private String iban;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BranchStatus status  = BranchStatus.ACTIVE;

    @OneToMany(mappedBy = "branch")
    private Set<Slot> slotSet;

    @OneToMany(mappedBy = "branch")
    private Set<WaitingList> waitingListSet;





}
