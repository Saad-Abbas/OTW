package com.example.otwAppservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productId;
    private String productName;
    private String firstLevelCategoryCode;
    private String firstLevelCategoryName;
    private String secondLevelCategoryCode;
    private String secondLevelCategoryName;
    private String thirdLevelCategoryCode;
    private String thirdLevelCategoryName;
    private Double referencePrice;
    private Double salesTaxRatePercentage;
    private String trainingStatus;
}
