package com.example.DoAnJava.service;

import com.example.DoAnJava.entity.CartItem;
import com.example.DoAnJava.entity.Order;
import com.example.DoAnJava.repository.OrderRepository;
import com.example.DoAnJava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public void createOrder(HttpSession session, String customerName, String phone, String email, String address, String username) {
        Map<String, CartItem> cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            return;
        }

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cartService.getTotalPrice(session));
        order.setStatus("PENDING");

        if (username != null) {
            userRepository.findByUsername(username).ifPresent(currentUser -> order.setCustomerName(currentUser.getFullName()));
        }

        orderRepository.save(order);
    }
}