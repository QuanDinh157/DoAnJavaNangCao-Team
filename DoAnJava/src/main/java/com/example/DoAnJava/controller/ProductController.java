package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Product;
import com.example.DoAnJava.service.CategoryService;
import com.example.DoAnJava.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        // Đã sửa thành "admin/product-list" để khớp với thư mục admin/
        return "admin/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        // Đã sửa thành "admin/product-form"
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "categoryId", required = false) String categoryId,
                              @RequestParam("imageFile") MultipartFile imageFile) {

        // 1. Xử lý ID trước tiên (đưa ra ngoài try-catch)
        if (product.getId() != null && product.getId().trim().isEmpty()) {
            product.setId(null);
        }

        // 2. Xử lý Category
        if (categoryId != null && !categoryId.isEmpty()) {
            product.setCategory(categoryService.getCategoryById(categoryId));
        }

        // 3. Xử lý Ảnh
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(imageFile.getInputStream(), filePath);
                product.setImageUrl(uniqueFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. Lưu
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        // Đã sửa thành "admin/product-form"
        return "admin/product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}