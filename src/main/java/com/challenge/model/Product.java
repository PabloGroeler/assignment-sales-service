package com.challenge.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder(toBuilder = true)
public class Product {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
