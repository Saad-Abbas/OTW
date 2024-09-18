package com.example.otwAppservice.entity.orders;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private String paymentStatus;  // Product List
    private String cartId;  // Card Id
    private String lbCustomerId;  // User Id

    // One Order can have multiple OrderDetails
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetails> orderDetails; // This will map the relationship with OrderDetails

}
