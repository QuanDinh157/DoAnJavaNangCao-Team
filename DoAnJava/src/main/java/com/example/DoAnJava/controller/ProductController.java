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

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "categoryId", required = false) String categoryId,
                              @RequestParam("imageFile") MultipartFile imageFile) {

        // 1. Xử lý ID
        if (product.getId() != null && product.getId().trim().isEmpty()) {
            product.setId(null);
        }

        // 2. Logic giữ ảnh cũ: Nếu là Edit (đã có ID) thì lấy lại URL cũ từ DB
        if (product.getId() != null) {
            Product existingProduct = productService.getProductById(product.getId());
            if (existingProduct != null) {
                product.setImageUrl(existingProduct.getImageUrl());
            }
        }

        // 3. Xử lý Category
        if (categoryId != null && !categoryId.isEmpty()) {
            product.setCategory(categoryService.getCategoryById(categoryId));
        }

        // 4. Xử lý Ảnh với Cloudinary (Chỉ ghi đè nếu có chọn file mới)
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                product.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5. Lưu sản phẩm
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/product-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}