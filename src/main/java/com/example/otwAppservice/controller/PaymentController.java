package com.example.otwAppservice.controller;

import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.mapper.MoyasarResponses.CardPaymentResponse;
import com.example.otwAppservice.service.feedbackService.FeedbackService;
import com.example.otwAppservice.service.userService.MoyasarService;
import com.example.otwAppservice.service.userService.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/otw/payment")
public class PaymentController {
    private static Logger LOGGER = LogManager.getLogger(FeedbackController.class);

    @Autowired
    MoyasarService moyasarService;

    @PostMapping("/cardPaymentViaCardDetails")
    public Map<String, Object> cardPaymentViaCardDetails() {
        Map<String, Object> responseMap = new HashMap<>();
        String statusCode = "00";
        String statusDescription = "Success";

        try {
            moyasarService.cardPaymentViaCardDetails();
//            Orders order = orderService.recordOrder(orderDTO);
//
//            if (order != null) {
//                LOGGER.info("Order Created Successfully");
//            } else {
//                statusCode = "0001";
//                statusDescription = "Failed to Create New Order - Validation Error in Order Details";
//            }
        } catch (Exception e) {
            LOGGER.error("Error while submitting order", e);
            statusCode = "0005";
            statusDescription = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }

        responseMap.put("code", statusCode);
        responseMap.put("message", statusDescription);
        return responseMap;
    }

    @PostMapping("/cardPaymentViaToken")
    public Map<String, Object> cardPaymentViaToken(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> responseMap = new HashMap<>();
        String statusCode = "00";
        String statusDescription = "Success";

        try {
            // Validate the token
            if (requestBody.get("token") == null || !(requestBody.get("token") instanceof String) || ((String) requestBody.get("token")).trim().isEmpty()) {
                statusCode = "02";
                statusDescription = "Invalid token: Token is required";
                throw new IllegalArgumentException(statusDescription);
            }
            String token = (String) requestBody.get("token");

            // Validate the amount
            if (requestBody.get("amount") == null || !(requestBody.get("amount") instanceof Integer) || (Integer) requestBody.get("amount") <= 0) {
                statusCode = "03";
                statusDescription = "Invalid amount: Amount must be a positive integer";
                throw new IllegalArgumentException(statusDescription);
            }
            int amount = (Integer) requestBody.get("amount");

            // Process the payment
            moyasarService.cardPaymentViaPaymentToken(token, amount);


        } catch (IllegalArgumentException e) {
            LOGGER.warn("Validation error: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error while processing payment", e);
            statusCode = "05";
            statusDescription = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }

        responseMap.put("code", statusCode);
        responseMap.put("message", statusDescription);
        return responseMap;
    }


    @PostMapping("/refundPayment")
    public Map<String, Object> refundPayment(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> responseMap = new HashMap<>();
        String statusCode = "00";
        String statusDescription = "Success";

        try {
            // Extracting payment ID and refund amount from the request body
            String paymentId = (String) requestBody.get("paymentId");
            int refundAmount = (Integer) requestBody.get("refundAmount");

            // Calling the refund API method

            CardPaymentResponse refundResponse = moyasarService.refundPayment(paymentId, refundAmount);

            if (refundResponse != null && "refunded".equals(refundResponse.getStatus())) {
                // Handle success
                LOGGER.info("Refund processed successfully for Payment ID: " + paymentId);
            } else {
                statusCode = "0002";
                statusDescription = "Refund failed";
                LOGGER.error("Refund failed for Payment ID: " + paymentId);
            }

        } catch (Exception e) {
            LOGGER.error("Error while processing refund", e);
            statusCode = "0005";
            statusDescription = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }

        responseMap.put("code", statusCode);
        responseMap.put("message", statusDescription);
        return responseMap;
    }

}
