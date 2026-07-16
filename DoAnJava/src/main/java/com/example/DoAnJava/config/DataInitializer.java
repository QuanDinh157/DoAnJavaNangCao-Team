package com.example.DoAnJava.config;

import com.example.DoAnJava.entity.Category;
import com.example.DoAnJava.entity.User;
import com.example.DoAnJava.repository.CategoryRepository;
import com.example.DoAnJava.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
        initializeCategories();
    }

    private void initializeAdminUser() {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("ROLE_ADMIN");

            userRepository.save(admin);
            logger.info("Default administrator account initialized successfully.");
        }
    }

    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            Category thucUong = new Category();
            thucUong.setName("Thức uống");
            categoryRepository.save(thucUong);

            Category banhNgot = new Category();
            banhNgot.setName("Bánh ngọt");
            categoryRepository.save(banhNgot);

            logger.info("Default categories initialized successfully.");
        }
    }
}