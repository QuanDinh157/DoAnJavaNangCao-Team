package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Product;
import com.example.DoAnJava.repository.BannerRepository;
import com.example.DoAnJava.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
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
    public String index(Model model) {
        List<Product> allProducts = productService.getAllProducts();
        List<Product> hotProducts = allProducts.stream()
                .filter(p -> p.getIsHot() != null && p.getIsHot())
                .collect(Collectors.toList());

        model.addAttribute("banners", bannerRepository.findAll());
        model.addAttribute("hotProducts", hotProducts);
        model.addAttribute("products", allProducts);
        model.addAttribute("activePage", "all");

        return "index";
    }

    @GetMapping("/thuc-uong")
    public String thucUong(Model model) {
        List<Product> drinks = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Thức uống".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", drinks);
        model.addAttribute("activePage", "thuc-uong");
        return "index";
    }

    @GetMapping("/banh-ngot")
    public String banhNgot(Model model) {
        List<Product> cakes = productService.getAllProducts().stream()
                .filter(p -> p.getCategory() != null && "Bánh ngọt".equalsIgnoreCase(p.getCategory().getName()))
                .collect(Collectors.toList());
        model.addAttribute("products", cakes);
        model.addAttribute("activePage", "banh-ngot");
        return "index";
    }
}