package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Product;
import com.example.DoAnJava.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Product> allProducts = productService.getAllProducts();

        // 1. Danh sách hot để hiển thị ở mục "SẢN PHẨM NỔI BẬT"
        List<Product> hotProducts = allProducts.stream()
                .filter(p -> p.getIsHot() != null && p.getIsHot())
                .collect(Collectors.toList());

        // 2. Truyền hotProducts vào model
        model.addAttribute("hotProducts", hotProducts);
        // Nếu cần hiển thị toàn bộ sp ở phần khác thì để "products", còn không thì thôi
        model.addAttribute("products", allProducts);

        return "index";
    }

    @GetMapping("/thuc-uong")
    public String thucUong(Model model) {
        List<Product> drinks = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Thức uống".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", drinks);
        return "index";
    }

    @GetMapping("/banh-ngot")
    public String banhNgot(Model model) {
        List<Product> cakes = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Bánh ngọt".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", cakes);
        return "index";
    }
}