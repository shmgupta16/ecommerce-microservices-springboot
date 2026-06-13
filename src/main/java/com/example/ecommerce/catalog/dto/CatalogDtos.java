package com.example.ecommerce.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CatalogDtos {
    public record CategoryResponse(Long id, String name, String slug) {}
    public record ProductResponse(Long id, String name, String description, String specifications, String imageUrl,
                                  BigDecimal price, int stockQuantity, CategoryResponse category) {}
    public record CategoryCreateRequest(@NotBlank String name, @NotBlank String slug) {}
    public record ProductCreateRequest(@NotBlank String name,
                                       @NotBlank String description,
                                       String specifications,
                                       String imageUrl,
                                       @NotNull @DecimalMin("0.01") BigDecimal price,
                                       @Min(0) int stockQuantity,
                                       @NotNull Long categoryId) {}
    public record ProductUpdateRequest(String name,
                                       String description,
                                       String specifications,
                                       String imageUrl,
                                       @DecimalMin("0.01") BigDecimal price,
                                       @Min(0) Integer stockQuantity,
                                       Long categoryId) {}
    public record InventoryAdjustmentRequest(@Min(0) int stockQuantity) {}
}
