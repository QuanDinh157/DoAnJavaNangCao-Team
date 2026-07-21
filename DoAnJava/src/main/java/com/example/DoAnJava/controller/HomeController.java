package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Product;
import com.example.DoAnJava.repository.BannerRepository;
import com.example.DoAnJava.service.CartService;
import com.example.DoAnJava.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;
    private final BannerRepository bannerRepository;
    private final CartService cartService;

    public HomeController(ProductService productService, BannerRepository bannerRepository, CartService cartService) {
        this.productService = productService;
        this.bannerRepository = bannerRepository;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<Product> allProducts = productService.getAllProducts();

        // Lọc ra các sản phẩm thực sự là HOT (hot = true)
        List<Product> hotProducts = allProducts.stream()
                .filter(p -> p.getHot() != null && p.getHot())
                .collect(Collectors.toList());

        model.addAttribute("banners", bannerRepository.findAll());
        model.addAttribute("hotProducts", hotProducts);

        // Gán danh sách hot vào biến "products" để hiển thị đúng mục Sản phẩm nổi bật ở index.html
        model.addAttribute("products", hotProducts);

        model.addAttribute("activePage", "all");
        model.addAttribute("cartCount", cartService.getCartCount(session));

        return "index";
    }

    @GetMapping("/thuc-uong")
    public String thucUong(Model model, HttpSession session) {
        List<Product> drinks = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Thức uống".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", drinks);
        model.addAttribute("activePage", "thuc-uong");
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "index";
    }

    @GetMapping("/banh-ngot")
    public String banhNgot(Model model, HttpSession session) {
        List<Product> cakes = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Bánh ngọt".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", cakes);
        model.addAttribute("activePage", "banh-ngot");
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "index";
    }

    @GetMapping("/ve-chung-toi")
    public String veChungToi(Model model) {
        return "about_us";
    }

    // ==========================================
    // CÁC ENDPOINT QUẢN LÝ GIỎ HÀNG
    // ==========================================

    @PostMapping("/cart/add/{id}")
    @ResponseBody
    public int addToCart(@PathVariable String id, HttpSession session) {
        cartService.addToCart(id, session);
        return cartService.getCartCount(session);
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cartItems", cartService.getCart(session));
        model.addAttribute("cartCount", cartService.getCartCount(session));
        model.addAttribute("total", cartService.getTotalPrice(session));
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable String id, HttpSession session) {
        cartService.removeItem(id, session);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{id}/{action}")
    public String updateQuantity(@PathVariable String id, @PathVariable String action, HttpSession session) {
        cartService.updateQuantity(id, action, session);
        return "redirect:/cart";
    }
}