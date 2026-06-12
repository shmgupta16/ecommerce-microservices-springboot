package com.example.ecommerce.catalog.dto;

import java.math.BigDecimal;

public class CatalogDtos {
    public record CategoryResponse(Long id, String name, String slug) {}
    public record ProductResponse(Long id, String name, String description, String specifications, String imageUrl,
                                  BigDecimal price, int stockQuantity, CategoryResponse category) {}
}
