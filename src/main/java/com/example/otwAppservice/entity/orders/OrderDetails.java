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
@Table(name = "OrderDetails")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String productId;
    private String productName; // The moment call merchant
    private Integer Quantity ; // The moment enter the store
    private String outTime;  // The moment leave the store
//    private String items;  // Product List
    private String cartId;  // Card Id
    private String lbCustomerId;  // User Id

}
