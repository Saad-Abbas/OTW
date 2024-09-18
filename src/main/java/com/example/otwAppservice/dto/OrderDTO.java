package com.example.otwAppservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private String storeId;
    private String checkout;
    private String entryTime;
    private String outTime;
    private List<OrderDetailsDTO> items;
    private String cartId;
    private String lbCustomerId;



    @Override
    public String toString() {
        return "OrderDTO{" +
                "storeId='" + storeId + '\'' +
                ", checkout='" + checkout + '\'' +
                ", entryTime='" + entryTime + '\'' +
                ", outTime='" + outTime + '\'' +
                ", items=" + items +
                ", cartId='" + cartId + '\'' +
                ", lbCustomerId='" + lbCustomerId + '\'' +
                '}';
    }
}
