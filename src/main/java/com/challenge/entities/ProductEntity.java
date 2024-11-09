package com.challenge.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@Document("product")
public class ProductEntity {

    @Id
    private String id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
