package com.example.DoAnJava.service;

import com.example.DoAnJava.entity.User;
import com.example.DoAnJava.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerNewUser(User user) {
        // Kiểm tra xem username hoặc email đã trùng chưa
        if (userRepository.findByUsername(user.getUsername()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent()) {
            return false; // Đăng ký thất bại do trùng dữ liệu
        }

        // Mã hóa mật khẩu bảo mật trước khi lưu vào MySQL
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Mặc định đăng ký mới là tài khoản khách hàng

        userRepository.save(user);
        return true;
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}