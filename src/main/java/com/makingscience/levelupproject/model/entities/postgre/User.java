package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.Role;
import com.makingscience.levelupproject.model.enums.UserStatus;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "user_account")
@RequiredArgsConstructor
public class User {

    @Id
    private UUID id;

    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "creation_date")
    private LocalDateTime creationDate = LocalDateTime.now(ZoneId.of("Asia/Tbilisi"));

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<UserCard> cardSet;
}