package com.makingscience.levelupproject.model.entities.postgre;

import com.makingscience.levelupproject.model.enums.CategoryStatus;
import com.makingscience.levelupproject.model.enums.Type;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "category")
@RequiredArgsConstructor
public class Category {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "commission")
    private Double commission;

    @Column(name = "name")
    private String name;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus categoryStatus;
}
