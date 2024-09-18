package com.example.otwAppservice.service.productService;

import com.example.otwAppservice.projectionClass.ProductPriceProjection;

import java.util.List;

public interface ProductService {
    public List<ProductPriceProjection> getOrderedProductPrices(List<String> productIds);
}
