package com.example.ecommerce.catalog;

import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    List<CategoryResponse> categories() {
        return catalogService.categories();
    }

    @GetMapping("/products")
    List<ProductResponse> products(@RequestParam(required = false) Long categoryId) {
        return catalogService.products(categoryId);
    }

    @GetMapping("/products/search")
    List<ProductResponse> search(@RequestParam String q) {
        return catalogService.search(q);
    }

    @GetMapping("/products/{id}")
    ProductResponse product(@PathVariable Long id) {
        return catalogService.product(id);
    }
}
