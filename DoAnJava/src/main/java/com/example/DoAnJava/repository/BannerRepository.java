package com.example.DoAnJava.repository;

import com.example.DoAnJava.entity.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BannerRepository extends MongoRepository<Banner, String> {
    List<Banner> findByActiveTrue();
}