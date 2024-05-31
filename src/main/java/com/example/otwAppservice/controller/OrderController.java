package com.example.otwAppservice.controller;

import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.service.orderService.OrderService;
import com.example.otwAppservice.utils.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    private static Logger LOGGER = LogManager.getLogger(OrderController.class);

    // Save Department Details
    @PostMapping("/resource/cloudpick/checkout")
    public Map<String, Object> recordOrder(@RequestBody OrderDTO orderDTO) {
        Map<String, Object> responseMap = new HashMap<>();
        String statusCode = "0000";
        String statusDescription = "Success";

        try {
            LOGGER.info("Order Body : " + orderDTO.toString());
            Orders order = orderService.recordOrder(orderDTO);

            if (order != null) {
                LOGGER.info("Order Created Successfully");
            } else {
                statusCode = "0001";
                statusDescription = "Failed to Create New Order - Validation Error in Order Details";
            }
        } catch (Exception e) {
            LOGGER.error("Error while submitting order", e);
            statusCode = "0005";
            statusDescription = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }

        responseMap.put("code", statusCode);
        responseMap.put("message", statusDescription);
        return responseMap;
    }
}
