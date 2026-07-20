package com.example.DoAnJava.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Order {
    @Id
    private String id;

    private String customerName;
    private String email;
    private String phoneNumber;
    private String shippingAddress;

    private Double totalAmount;
    private String status; // Ví dụ: PENDING, PAID, CANCELLED
    private LocalDateTime orderDate;

    // MongoDB lưu chi tiết sản phẩm lồng trong đơn hàng.
    private List<OrderDetail> orderDetails;
}
