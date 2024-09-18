package com.example.otwAppservice.service.productService;

import com.example.otwAppservice.controller.LoginController;
import com.example.otwAppservice.projectionClass.ProductPriceProjection;
import com.example.otwAppservice.repository.OrderDetailsRepository;
import com.example.otwAppservice.repository.OrderRepository;
import com.example.otwAppservice.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger LOGGER = LogManager.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<ProductPriceProjection> getOrderedProductPrices(List<String> productIds) {
        return productRepository.findProductPricesByIds(productIds);
    }

}
