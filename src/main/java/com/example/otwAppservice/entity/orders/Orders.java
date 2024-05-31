package com.example.otwAppservice.entity.orders;


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
@Table(name = "Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String storeId;
    private String checkOutTime; // The moment call merchant
    private String entryTime; // The moment enter the store
    private String outTime;  // The moment leave the store
    private String items;  // Product List
    private String cartId;  // Card Id
    private String lbCustomerId;  // User Id
}
