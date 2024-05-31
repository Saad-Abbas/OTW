package com.example.otwAppservice.service.orderService;

import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.entity.orders.Orders;

public interface OrderService {
    Orders recordOrder(OrderDTO orderDTO);
}
