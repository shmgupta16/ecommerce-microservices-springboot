package com.example.ecommerce.config;

import com.example.ecommerce.catalog.Category;
import com.example.ecommerce.catalog.CategoryRepository;
import com.example.ecommerce.catalog.Product;
import com.example.ecommerce.catalog.ProductRepository;
import com.example.ecommerce.user.AppUser;
import com.example.ecommerce.user.UserRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedCatalog(CategoryRepository categoryRepository, ProductRepository productRepository,
                                  UserRepository userRepository, PasswordEncoder passwordEncoder,
                                  @Value("${app.seed.admin-email}") String adminEmail,
                                  @Value("${app.seed.admin-password}") String adminPassword) {
        return args -> {
            if (!userRepository.existsByEmail(adminEmail)) {
                AppUser admin = new AppUser();
                admin.setEmail(adminEmail);
                admin.setFullName("Development Admin");
                admin.setPasswordHash(passwordEncoder.encode(adminPassword));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }

            if (categoryRepository.count() > 0) {
                return;
            }
            Category electronics = category("Electronics", "electronics");
            Category fashion = category("Fashion", "fashion");
            categoryRepository.save(electronics);
            categoryRepository.save(fashion);

            productRepository.save(product("Wireless Headphones", "Noise cancelling Bluetooth headphones",
                    "Bluetooth 5.3, 40h battery, USB-C charging", "https://example.com/images/headphones.jpg",
                    new BigDecimal("4999.00"), 50, electronics));
            productRepository.save(product("Smart Watch", "Fitness and notification smart watch",
                    "AMOLED display, heart-rate tracking, 5ATM water resistance", "https://example.com/images/watch.jpg",
                    new BigDecimal("7999.00"), 25, electronics));
            productRepository.save(product("Cotton T-Shirt", "Comfort fit cotton t-shirt",
                    "100 percent cotton, regular fit", "https://example.com/images/tshirt.jpg",
                    new BigDecimal("799.00"), 100, fashion));
        };
    }

    private Category category(String name, String slug) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        return category;
    }

    private Product product(String name, String description, String specs, String imageUrl, BigDecimal price,
                            int stock, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setSpecifications(specs);
        product.setImageUrl(imageUrl);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setCategory(category);
        return product;
    }
}
