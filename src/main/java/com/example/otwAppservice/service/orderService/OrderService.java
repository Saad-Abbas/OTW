package com.example.otwAppservice.service.orderService;

import com.example.otwAppservice.dto.DuplicateException;
import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.projectionClass.CustomerOrderResponseProjection;
import com.example.otwAppservice.projectionClass.OrderProductProjection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    Optional<Orders> recordOrder(OrderDTO orderDTO) throws DuplicateException;

    List<OrderProductProjection> getOrderDetailsByCartId(String cartId);

    Map<String, Object> prepareOrderDetailsResponse(String cartId, List<OrderProductProjection> orderDetails);
     List<Orders> getOrdersByCustomerId(String lbCustomerId);
     List<CustomerOrderResponseProjection> getOrderDetailsByCustomerId(String customerId);
}
