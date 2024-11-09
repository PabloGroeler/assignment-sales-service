package com.challenge.mapper;

import com.challenge.entities.ProductEntity;
import com.challenge.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductEntity mapToProductEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public Product mapToProductModel(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();
    }
}
