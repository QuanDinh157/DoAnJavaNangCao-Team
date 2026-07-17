package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.CartItem;
import com.example.DoAnJava.entity.Product;
import com.example.DoAnJava.repository.BannerRepository;
import com.example.DoAnJava.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;
    private final BannerRepository bannerRepository;

    public HomeController(ProductService productService, BannerRepository bannerRepository) {
        this.productService = productService;
        this.bannerRepository = bannerRepository;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<Product> allProducts = productService.getAllProducts();
        List<Product> hotProducts = allProducts.stream()
                .filter(p -> p.getIsHot() != null && p.getIsHot())
                .collect(Collectors.toList());

        model.addAttribute("banners", bannerRepository.findAll());
        model.addAttribute("hotProducts", hotProducts);
        model.addAttribute("products", allProducts);
        model.addAttribute("activePage", "all");
        model.addAttribute("cartCount", getCartCount(session));

        return "index";
    }

    @GetMapping("/thuc-uong")
    public String thucUong(Model model, HttpSession session) {
        List<Product> drinks = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Thức uống".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", drinks);
        model.addAttribute("activePage", "thuc-uong");
        model.addAttribute("cartCount", getCartCount(session));
        return "index";
    }

    @GetMapping("/banh-ngot")
    public String banhNgot(Model model, HttpSession session) {
        List<Product> cakes = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Bánh ngọt".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", cakes);
        model.addAttribute("activePage", "banh-ngot");
        model.addAttribute("cartCount", getCartCount(session));
        return "index";
    }
    @GetMapping("/ve-chung-toi")
    public String veChungToi(Model model) {
        return "about_us";
    }

    // Hàm thêm vào giỏ
    @PostMapping("/cart/add/{id}")
    @ResponseBody // Dòng này cực kỳ quan trọng để không load lại trang
    public int addToCart(@PathVariable String id, HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();

        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            Product product = productService.getProductById(id);
            if (product != null) {
                // Lưu ý: Đảm bảo CartItem của bạn có setter cho quantity hoặc khởi tạo đúng
                cart.put(id, new CartItem(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()));
            }
        }
        session.setAttribute("cart", cart);
        return getCartCount(session);
    }

    private int getCartCount(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) return 0;
        int totalCount = 0;
        for (CartItem item : cart.values()) {
            totalCount += item.getQuantity();
        }
        return totalCount;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        model.addAttribute("cartItems", cart != null ? cart : new HashMap<>());
        model.addAttribute("cartCount", getCartCount(session));

        // Tính tổng tiền
        double total = 0;
        if (cart != null) {
            for (CartItem item : cart.values()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        model.addAttribute("total", total); // Gửi biến total sang HTML
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable String id, HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(id);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }
    @PostMapping("/cart/update/{id}/{action}")
    public String updateQuantity(@PathVariable String id, @PathVariable String action, HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart != null && cart.containsKey(id)) {
            CartItem item = cart.get(id);
            if ("increase".equals(action)) {
                item.setQuantity(item.getQuantity() + 1);
            } else if ("decrease".equals(action) && item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            }
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

}