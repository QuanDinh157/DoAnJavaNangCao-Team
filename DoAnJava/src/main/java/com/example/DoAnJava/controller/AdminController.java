package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Banner;
import com.example.DoAnJava.entity.Category;
import com.example.DoAnJava.repository.BannerRepository;
import com.example.DoAnJava.service.CategoryService;
import com.example.DoAnJava.service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final CategoryService categoryService;
    private final BannerRepository bannerRepository;
    private final CloudinaryService cloudinaryService;

    public AdminController(CategoryService categoryService, BannerRepository bannerRepository, CloudinaryService cloudinaryService) {
        this.categoryService = categoryService;
        this.bannerRepository = bannerRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/category-list";
    }

    @GetMapping("/categories/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String showEditCategoryForm(@PathVariable String id, Model model) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            model.addAttribute("category", category);
        }
        return "admin/category-form";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}