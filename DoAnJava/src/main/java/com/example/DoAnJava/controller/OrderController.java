package com.example.DoAnJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

import com.example.DoAnJava.service.CartService;
import com.example.DoAnJava.service.OrderService;
import com.example.DoAnJava.service.UserService;
import com.example.DoAnJava.entity.User;
import com.example.DoAnJava.entity.CartItem;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model, Principal principal) {
        Map<String, CartItem> cart = cartService.getCart(session);

        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            if (currentUser != null) {
                model.addAttribute("savedName", currentUser.getFullName());
                model.addAttribute("savedEmail", currentUser.getEmail());
                model.addAttribute("savedPhone", currentUser.getPhone());
            }
        }

        model.addAttribute("cartItems", cart);
        model.addAttribute("totalPrice", cartService.getTotalPrice(session));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @RequestParam("customerName") String customerName,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            HttpSession session,
            Principal principal) {

        orderService.createOrder(session, customerName, phone, email, address, principal != null ? principal.getName() : null);
        cartService.clearCart(session);

        return "redirect:/order-success";
    }

    @GetMapping("/order-success")
    public String showOrderSuccess() {
        return "order_success";
    }

    @GetMapping("/checkout-now")
    public String checkoutNow(@RequestParam("productId") String productId, HttpSession session) {
        cartService.addToCart(productId, session);
        return "redirect:/checkout";
    }
}