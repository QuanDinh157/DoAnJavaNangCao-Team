package com.example.DoAnJava.service;

import com.example.DoAnJava.entity.CartItem;
import com.example.DoAnJava.entity.Order;
import com.example.DoAnJava.entity.OrderDetail;
import com.example.DoAnJava.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order placeOrder(String customerName, String email, String phone, String address, List<CartItem> cartItems) {
        // 1. Tạo đối tượng Order
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setEmail(email);
        order.setPhoneNumber(phone);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // 2. Chuyển đổi CartItem sang OrderDetail
        List<OrderDetail> details = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(item.getProductId());
            detail.setProductName(item.getName());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            return detail;
        }).collect(Collectors.toList());

        order.setOrderDetails(details);

        // 3. Tính tổng tiền
        double total = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(total);

        // 4. MongoDB lưu cả Order và danh sách OrderDetail trong một document.
        return orderRepository.save(order);
    }
}
