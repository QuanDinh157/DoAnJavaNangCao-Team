package com.example.DoAnJava.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    private Boolean isHot = false;

    private Integer viewCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();


    private Category category;
}