package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.MerchantStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "merchant")
@RequiredArgsConstructor
public class Merchant {

    @Id
    private UUID id;

    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Column(name = "created_at")
    private LocalDateTime createDate = LocalDateTime.now(ZoneId.of("Asia/Tbilisi"));

    @ManyToOne
    @JoinColumn(name = "category_id" , referencedColumnName = "id")
    private Category category;


    @OneToMany(mappedBy = "merchant")
    private Set<Branch> branchSet;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "iban")
    private String iban;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "image")
    private String image;

    @Column(name = "document_address")
    private String documentAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MerchantStatus status = MerchantStatus.ACTIVE;





}
