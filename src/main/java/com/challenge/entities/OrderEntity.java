package com.challenge.entities;

import com.challenge.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Document("order")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity implements Serializable {
    @Id
    private String id;
    private String idempotencyKey;
    private List<ProductEntity> products;
    private StatusEnum status;
    private BigDecimal totalAmount;

}