package com.example.otwAppservice.repository;
import com.example.otwAppservice.projectionClass.OrderProductProjection;
import com.example.otwAppservice.entity.Products;
import com.example.otwAppservice.projectionClass.ProductPriceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {

    @Query("SELECT p.productId AS productId, p.referencePrice AS referencePrice " +
            "FROM Products p " +
            "WHERE p.productId IN :productIds")
    List<ProductPriceProjection> findProductPricesByIds(@Param("productIds") List<String> productIds);


}
