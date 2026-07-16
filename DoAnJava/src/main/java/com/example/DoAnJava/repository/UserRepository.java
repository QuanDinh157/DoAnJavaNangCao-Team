package com.example.DoAnJava.repository;

import com.example.DoAnJava.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository; // Đổi import ở đây
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}