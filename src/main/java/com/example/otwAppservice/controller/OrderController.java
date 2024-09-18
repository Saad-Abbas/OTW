package com.example.otwAppservice.controller;

import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.projectionClass.CustomerOrderResponseProjection;
import com.example.otwAppservice.projectionClass.OrderProductProjection;
import com.example.otwAppservice.service.orderService.OrderService;
import com.example.otwAppservice.utils.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    // get Order Details
    @RequestMapping("/getOrderDetails")
    public ResponseEntity getOrderDetails(@RequestParam String cartId) {
        ResponseEntity r = null;
        Map<String, Object> responseMap = new HashMap<>();
        try {
            LOGGER.info("Cart Id: " + cartId);
            List<OrderProductProjection> order = orderService.getOrderDetailsByCartId(cartId);

            if (order != null) {
                LOGGER.info("Order Fetched Successfully");
                responseMap = orderService.prepareOrderDetailsResponse(cartId, order);
                r = ResponseEntity.ok().body(new Messages<>().setMessage("Order Fetched successfully.").setData(responseMap).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            } else {
                LOGGER.info("Invalid Cart-ID. \nFailed to Fetch Order.");
                r = ResponseEntity.ok().body(new Messages<>().setMessage("No Order found with CardId : " + cartId).setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            }
        } catch (Exception e) {
            r = ResponseEntity.internalServerError().body(new Messages<>().setMessage("Exception Error : " + e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }

        return r;
    }

    // get Order Details
    @RequestMapping("/getOrderDetailsByCustomerId")
    public ResponseEntity getOrderDetailsByCustomerId(@RequestParam String customerId) {
        ResponseEntity r = null;
        Map<String, Object> responseMap = new HashMap<>();
        try {
            LOGGER.info("Customer Id: " + customerId);
            List<CustomerOrderResponseProjection> order = orderService.getOrderDetailsByCustomerId(customerId);

            if (order != null && order.size() > 0) {
                LOGGER.info("Order Fetched Successfully");
//                responseMap = orderService.prepareOrderDetailsResponse(cartId, order);
                r = ResponseEntity.ok().body(new Messages<>().setMessage("Order Fetched successfully.").setData(order).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            } else {
                LOGGER.info("Invalid Customer-ID. \nFailed to Fetch Orders.");
                r = ResponseEntity.ok().body(new Messages<>().setMessage("No Order found with CustomerId : " + customerId).setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            }
        } catch (Exception e) {
            r = ResponseEntity.internalServerError().body(new Messages<>().setMessage("Exception Error : " + e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }

        return r;
    }
}
