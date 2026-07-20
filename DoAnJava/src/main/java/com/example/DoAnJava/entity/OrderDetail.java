package com.example.DoAnJava.entity;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrderDetail {
    private String productId;
    private String productName;
    private int quantity;
    private Double price;
}
