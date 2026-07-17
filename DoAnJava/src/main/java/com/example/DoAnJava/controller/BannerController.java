package com.example.DoAnJava.controller;

import com.example.DoAnJava.entity.Banner;
import com.example.DoAnJava.repository.BannerRepository;
import com.example.DoAnJava.service.CloudinaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/admin/banners")
public class BannerController {

    private final BannerRepository bannerRepository;
    private final CloudinaryService cloudinaryService;

    public BannerController(BannerRepository bannerRepository, CloudinaryService cloudinaryService) {
        this.bannerRepository = bannerRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public String listBanners(Model model) {
        model.addAttribute("banners", bannerRepository.findAll());
        return "admin/banner-list";
    }

    @PostMapping("/save")
    public String saveBanner(@RequestParam("title") String title,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if (!imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                Banner banner = new Banner();
                banner.setTitle(title);
                banner.setImageUrl(imageUrl);
                banner.setActive(true);
                bannerRepository.save(banner);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/banners";
    }

    @GetMapping("/delete/{id}")
    public String deleteBanner(@PathVariable String id) {
        bannerRepository.deleteById(id);
        return "redirect:/admin/banners";
    }
}