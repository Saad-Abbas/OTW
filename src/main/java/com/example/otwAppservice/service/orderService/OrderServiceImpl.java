package com.example.otwAppservice.service.orderService;


import com.example.otwAppservice.controller.LoginController;
import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.dto.OrderDetailsDTO;
import com.example.otwAppservice.entity.orders.OrderDetails;
import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.repository.OrderDetailsRepository;
import com.example.otwAppservice.repository.OrderRepository;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private static Logger LOGGER = LogManager.getLogger(LoginController.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Override
    public Orders recordOrder(OrderDTO orderDTO) {
        Orders order = convertToOrderEntity(orderDTO);

        // Save order
        order = orderRepository.save(order);
        int index = 0;
        if (order != null && orderDTO.getItems() != null && !orderDTO.getItems().isEmpty()) {
            LOGGER.info("Order Saved Successfully");

            for (OrderDetailsDTO item : orderDTO.getItems()) {
                index++;
                if (!saveOrderDetails(order, item)) {
                    // If any order details validation fails, return null indicating an issue
                    LOGGER.info("Failed to Save Item :" + index + " Product-Code : " + item.getUpc() + "");
                    return null;
                } else {
                    LOGGER.info("Item :" + index + " Product-Code : " + item.getUpc() + " Saved Successfully");
                }
            }
        }

        return order;
    }

    private Orders convertToOrderEntity(OrderDTO orderDTO) {
        Orders order = null;
        try {
            order = new Orders();
            order.setEntryTime(orderDTO.getEntryTime());
            order.setCartId(orderDTO.getCartId());
            order.setStoreId(orderDTO.getStoreId());
            order.setOutTime(orderDTO.getOutTime());
            order.setCheckOutTime(orderDTO.getCheckout());
            order.setLbCustomerId(orderDTO.getLbCustomerId());
            order.setItems(orderDTO.getItems().toString()); // Consider serializing the list properly
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    private boolean saveOrderDetails(Orders order, OrderDetailsDTO item) {
        if (item.getUpc() == null || item.getUpc().isEmpty()) {
            // Log the issue and return false indicating validation failure
            LOGGER.error("Order details validation failed: Missing UPC Code ");
            return false;
        }

        try {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setCartId(order.getCartId());
            orderDetails.setOrderId(order.getId());
            orderDetails.setProductId(item.getUpc());
            orderDetails.setLbCustomerId(order.getLbCustomerId());
            orderDetails.setOutTime(order.getOutTime());
            orderDetails.setQuantity(item.getQty());
            // orderDetails.setProductName(item.getPicUrl()); // Uncomment if needed

            orderDetailsRepository.save(orderDetails);
        } catch (Exception e) {
            LOGGER.error(e.getStackTrace());
        }
//        LOGGER.info("Product-Code : " + item.getUpc() + " Saved Successfully");
        return true;
    }

}
