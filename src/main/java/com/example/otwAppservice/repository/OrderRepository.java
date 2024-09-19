package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.orders.Orders;
import com.example.otwAppservice.projectionClass.OrderProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    @Query("SELECT new com.example.otwAppservice.projectionClass.OrderProductProjection(od.productId, p.productName, p.referencePrice,od.Quantity) " +
            "FROM Orders o " +
            "JOIN OrderDetails od ON od.order.id = o.id " +
            "JOIN Products p ON p.productId = od.productId " +
            "WHERE o.cartId = :cartId")
    List<OrderProductProjection> findOrderDetailsByCartId(@Param("cartId") String cartId);

    List<Orders> findByLbCustomerIdOrderByIdDesc(String lbCustomerId);

    @Query(value = "SELECT o.cart_id, od.product_id, p.product_name, p.reference_price,od.quantity " +
            "FROM orders o " +
            "INNER JOIN order_details od ON od.order_id = o.id " +
            "INNER JOIN products p ON p.product_id = od.product_id " +
            "WHERE o.lb_customer_id = :customerId " +
            "ORDER BY o.id DESC", nativeQuery = true)
    List<Object[]> findOrdersByCustomerId(@Param("customerId") String customerId);

}
