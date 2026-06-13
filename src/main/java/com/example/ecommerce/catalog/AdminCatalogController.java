package com.example.ecommerce.catalog;

import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryCreateRequest;
import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.InventoryAdjustmentRequest;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductCreateRequest;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/catalog")
public class AdminCatalogController {
    private final CatalogService catalogService;

    public AdminCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping("/categories")
    CategoryResponse createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        return catalogService.createCategory(request);
    }

    @PostMapping("/products")
    ProductResponse createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return catalogService.createProduct(request);
    }

    @PutMapping("/products/{productId}")
    ProductResponse updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductUpdateRequest request) {
        return catalogService.updateProduct(productId, request);
    }

    @PatchMapping("/products/{productId}/inventory")
    ProductResponse adjustInventory(@PathVariable Long productId,
                                    @Valid @RequestBody InventoryAdjustmentRequest request) {
        return catalogService.adjustInventory(productId, request);
    }
}
