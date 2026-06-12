package com.example.ecommerce.catalog;

import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CatalogService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public CatalogService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> categories() {
        return categoryRepository.findAll().stream().map(CatalogMapper::toResponse).toList();
    }

    public List<ProductResponse> products(Long categoryId) {
        List<Product> products = categoryId == null ? productRepository.findAll() : productRepository.findByCategoryId(categoryId);
        return products.stream().map(CatalogMapper::toResponse).toList();
    }

    public List<ProductResponse> search(String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream().map(CatalogMapper::toResponse).toList();
    }

    public ProductResponse product(Long id) {
        return CatalogMapper.toResponse(load(id));
    }

    public Product load(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
}
