package com.example.ecommerce.catalog;

import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductResponse;

public final class CatalogMapper {
    private CatalogMapper() {
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getSlug());
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSpecifications(),
                product.getImageUrl(),
                product.getPrice(),
                product.getStockQuantity(),
                toResponse(product.getCategory()));
    }
}
