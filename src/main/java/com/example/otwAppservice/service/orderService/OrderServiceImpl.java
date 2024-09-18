package com.example.otwAppservice.service.orderService;


import com.example.otwAppservice.controller.LoginController;
import com.example.otwAppservice.dto.OrderDTO;
import com.example.otwAppservice.dto.OrderDetailsDTO;
import com.example.otwAppservice.entity.orders.OrderDetails;
import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.projectionClass.CustomerOrderResponseProjection;
import com.example.otwAppservice.projectionClass.OrderProductProjection;
import com.example.otwAppservice.projectionClass.ProductPriceProjection;
import com.example.otwAppservice.repository.OrderDetailsRepository;
import com.example.otwAppservice.repository.OrderRepository;
import com.example.otwAppservice.repository.ProductRepository;
import com.example.otwAppservice.service.productService.ProductService;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private static Logger LOGGER = LogManager.getLogger(LoginController.class);
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderDetailsRepository orderDetailsRepository;

    @Override
    public Orders recordOrder(OrderDTO orderDTO) {
        Orders order = convertToOrderEntity(orderDTO);
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            LOGGER.info("Order items are empty");
            return null;
        }

        // Save order
        order = orderRepository.save(order);
        if (order == null) {
            LOGGER.error("Failed to save order");
            return null;
        }

        // Save order details in bulk
        List<OrderDetails> savedOrderDetails = saveOrderDetails(order, orderDTO.getItems());
        if (savedOrderDetails == null) {
            LOGGER.error("Failed to save order details");
            return null;
        }


        return order;
    }

    private Orders convertToOrderEntity(OrderDTO orderDTO) {
        Orders order = null;
        try {
            order = new Orders();
            order.setCartId(orderDTO.getCartId());
            order.setStoreId(orderDTO.getStoreId());
            order.setCheckOutTime(orderDTO.getCheckout());
            order.setLbCustomerId(orderDTO.getLbCustomerId());
//            order.setItems(orderDTO.getItems().toString()); // Consider serializing the list properly
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

//    private boolean saveOrderDetails(Orders order, OrderDetailsDTO item) {
//        if (item.getUpc() == null || item.getUpc().isEmpty()) {
//            // Log the issue and return false indicating validation failure
//            LOGGER.error("Order details validation failed: Missing UPC Code ");
//            return false;
//        }
//
//        try {
//            OrderDetails orderDetails = new OrderDetails();
//            orderDetails.setCartId(order.getCartId());
//            orderDetails.setOrderId(order.getId());
//            orderDetails.setProductId(item.getUpc());
//            orderDetails.setLbCustomerId(order.getLbCustomerId());
//            orderDetails.setOutTime(order.getOutTime());
//            orderDetails.setQuantity(item.getQty());
//            // orderDetails.setProductName(item.getPicUrl()); // Uncomment if needed
//
//            orderDetailsRepository.save(orderDetails);
//        } catch (Exception e) {
//            LOGGER.error(e.getStackTrace());
//        }
////        LOGGER.info("Product-Code : " + item.getUpc() + " Saved Successfully");
//        return true;
//    }
//

    private List<OrderDetails> saveOrderDetails(Orders order, List<OrderDetailsDTO> items) {
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderDetailsDTO item : items) {
            if (item.getUpc() == null || item.getUpc().isEmpty()) {
                LOGGER.error("Order details validation failed: Missing UPC Code for item with quantity: " + item.getQty());
                return null; // Returning null if any validation fails
            }

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setCartId(order.getCartId());
            orderDetails.setOrder(order);
            orderDetails.setProductId(item.getUpc());
            orderDetails.setLbCustomerId(order.getLbCustomerId());
            orderDetails.setQuantity(item.getQty());
            // orderDetails.setProductName(item.getPicUrl()); // Uncomment if needed

            orderDetailsList.add(orderDetails);
        }

        try {
            // Save all order details in a single batch operation
            orderDetailsRepository.saveAll(orderDetailsList);
            LOGGER.info("All order details saved successfully.");
        } catch (Exception e) {
            LOGGER.error("Error saving order details: " + e.getMessage(), e);
            return null; // Return null or handle this according to your error strategy
        }

        return orderDetailsList;
    }

    @Override
    public List<OrderProductProjection> getOrderDetailsByCartId(String cartId) {
        return orderRepository.findOrderDetailsByCartId(cartId);
    }

    @Override
    public Map<String, Object> prepareOrderDetailsResponse(String cartId, List<OrderProductProjection> orderDetails) {

        // Create a map for 'data' with the cartId
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("cartId", cartId);
        Double totalPrice = 0.0;
        int itemCount = 0;
        // Create a list for productDetails
        List<Map<String, Object>> productDetails = new ArrayList<>();
        for (OrderProductProjection product : orderDetails) {
            Map<String, Object> productMap = new HashMap<>();

            productMap.put("productId", product.getProductId());
            productMap.put("productName", product.getProductName());
            productMap.put("referencePrice", product.getReferencePrice());
            totalPrice += product.getReferencePrice();
            itemCount++;
            productDetails.add(productMap);
        }
        dataMap.put("paymentStatus", "paid");
        dataMap.put("productDetails", productDetails);
        dataMap.put("totalPrice", totalPrice);
        dataMap.put("itemCount", itemCount);

        return dataMap;
    }

    @Override
    public List<Orders> getOrdersByCustomerId(String lbCustomerId) {
        return orderRepository.findByLbCustomerIdOrderByIdDesc(lbCustomerId);
    }


    // This Function Includes dynamic Response as per customer needs
    @Override
    public List<CustomerOrderResponseProjection> getOrderDetailsByCustomerId(String customerId) {
        List<Object[]> results = orderRepository.findOrdersByCustomerId(customerId);
        Map<String, CustomerOrderResponseProjection> orderMap = new HashMap<>();

        // Process the results
        for (Object[] row : results) {
            String cartId = (String) row[0];
            String productId = (String) row[1];
            String productName = (String) row[2];
            Double referencePrice = (Double) row[3];
            Integer quantity = (row[4] != null) ? (Integer) row[4] : 0;


            // Check if we already have this cartId in the map
            CustomerOrderResponseProjection orderResponse = orderMap.getOrDefault(cartId, new CustomerOrderResponseProjection());
            orderResponse.setCartId(cartId);
            orderResponse.setPaymentStatus("paid");



            // Add product details to the list
            OrderProductProjection productDetail = new OrderProductProjection(productId, productName, referencePrice, quantity);
            productDetail.setReferencePrice(productDetail.getReferencePrice() * productDetail.getQuantity());
            orderResponse.getProductDetails().add(productDetail);

            // Calculate total price and item count
            orderResponse.setTotalPrice(orderResponse.getTotalPrice() + productDetail.getReferencePrice());
            orderResponse.setItemCount(orderResponse.getItemCount() + 1);
            // Update the map
            orderMap.put(cartId, orderResponse);
        }

        return new ArrayList<>(orderMap.values());
    }

}
