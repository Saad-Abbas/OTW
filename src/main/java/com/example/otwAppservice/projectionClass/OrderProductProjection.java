package com.example.otwAppservice.projectionClass;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductProjection {
    private String cartId;
    private String productId;
    private String productName;
    private Integer quantity;
    private Double referencePrice;
    private Double unitPrice;


    public OrderProductProjection(String productId, String productName, Double referencePrice,Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.referencePrice = referencePrice;
        this.unitPrice = referencePrice;
        this.quantity = quantity;
    }

}