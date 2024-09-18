package com.example.otwAppservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    private String upc;
    private String picUrl;
    private int qty;

    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
                "upc='" + upc + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", qty=" + qty +
                '}';
    }
}
