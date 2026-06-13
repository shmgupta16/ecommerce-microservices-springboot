package com.example.ecommerce.catalog;

import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.CategoryCreateRequest;
import com.example.ecommerce.catalog.dto.CatalogDtos.InventoryAdjustmentRequest;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductCreateRequest;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductResponse;
import com.example.ecommerce.catalog.dto.CatalogDtos.ProductUpdateRequest;
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

    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsBySlug(request.slug())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category slug already exists");
        }
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.slug().toLowerCase());
        return CatalogMapper.toResponse(categoryRepository.save(category));
    }

    public ProductResponse createProduct(ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setSpecifications(request.specifications());
        product.setImageUrl(request.imageUrl());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(loadCategory(request.categoryId()));
        return CatalogMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = load(productId);
        if (request.name() != null) {
            product.setName(request.name());
        }
        if (request.description() != null) {
            product.setDescription(request.description());
        }
        if (request.specifications() != null) {
            product.setSpecifications(request.specifications());
        }
        if (request.imageUrl() != null) {
            product.setImageUrl(request.imageUrl());
        }
        if (request.price() != null) {
            product.setPrice(request.price());
        }
        if (request.stockQuantity() != null) {
            product.setStockQuantity(request.stockQuantity());
        }
        if (request.categoryId() != null) {
            product.setCategory(loadCategory(request.categoryId()));
        }
        return CatalogMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse adjustInventory(Long productId, InventoryAdjustmentRequest request) {
        Product product = load(productId);
        product.setStockQuantity(request.stockQuantity());
        return CatalogMapper.toResponse(productRepository.save(product));
    }

    public Product load(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    private Category loadCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }
}
