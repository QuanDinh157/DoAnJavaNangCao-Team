package com.example.DoAnJava.service;

import com.example.DoAnJava.entity.CartItem;
import com.example.DoAnJava.entity.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }


    public Map<String, CartItem> getCart(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        return cart;
    }


    public void addToCart(String productId, HttpSession session) {
        Map<String, CartItem> cart = getCart(session);

        if (cart.containsKey(productId)) {
            CartItem item = cart.get(productId);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            Product product = productService.getProductById(productId);
            if (product != null) {
                cart.put(productId, new CartItem(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()));
            }
        }
        session.setAttribute("cart", cart);
    }


    public void updateQuantity(String productId, String action, HttpSession session) {
        Map<String, CartItem> cart = getCart(session);
        if (cart.containsKey(productId)) {
            CartItem item = cart.get(productId);
            if ("increase".equals(action)) {
                item.setQuantity(item.getQuantity() + 1);
            } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
            session.setAttribute("cart", cart);
        }
    }


    public void removeItem(String productId, HttpSession session) {
        Map<String, CartItem> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute("cart", cart);
    }


    public int getCartCount(HttpSession session) {
        return getCart(session).values().stream().mapToInt(CartItem::getQuantity).sum();
    }


    public double getTotalPrice(HttpSession session) {
        return getCart(session).values().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }


    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }
}