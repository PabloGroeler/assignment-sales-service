package com.challenge.model;

import com.challenge.enums.StatusEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Order implements Serializable {

    private String id;
    private List<Product> products;
    private StatusEnum status;
    private BigDecimal totalAmount;
}
