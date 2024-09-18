package com.example.otwAppservice.projectionClass;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderResponseProjection {
    private Double totalPrice = 0.0;
    private String cartId;
    private String orderTime;
    private String storeName;
    private String paymentStatus;
    private Integer itemCount = 0;
    private List<OrderProductProjection> productDetails = new ArrayList<>();

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public List<OrderProductProjection> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<OrderProductProjection> productDetails) {
        this.productDetails = productDetails;
    }
}
