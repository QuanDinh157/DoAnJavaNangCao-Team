package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Product;
import com.example.DoAnJava.service.CategoryService;
import com.example.DoAnJava.service.CloudinaryService;
import com.example.DoAnJava.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    public ProductController(ProductService productService, CategoryService categoryService, CloudinaryService cloudinaryService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "categoryId", required = false) String categoryId,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam(value = "hotCheckbox", required = false) String hotCheckbox) {

        // XỬ LÝ LOGIC CHECKBOX: Nếu biến hotCheckbox null (không chọn) -> false. Ngược lại -> true
        boolean isHot = "true".equals(hotCheckbox);
        product.setHot(isHot);

        // Xử lý tạo mới (ID rỗng)
        if (product.getId() != null && product.getId().trim().isEmpty()) {
            product.setId(null);
        }

        // Bảo toàn dữ liệu cũ khi Edit
        if (product.getId() != null) {
            Product existingProduct = productService.getProductById(product.getId());
            if (existingProduct != null) {
                product.setImageUrl(existingProduct.getImageUrl());
                product.setViewCount(existingProduct.getViewCount());
                product.setCreatedAt(existingProduct.getCreatedAt());
            }
        }

        // Set Category
        if (categoryId != null && !categoryId.isEmpty()) {
            product.setCategory(categoryService.getCategoryById(categoryId));
        }

        // Upload ảnh lên Cloudinary
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                product.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            System.err.println("Lỗi upload ảnh: " + e.getMessage());
        }

        // Lưu xuống MongoDB
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}