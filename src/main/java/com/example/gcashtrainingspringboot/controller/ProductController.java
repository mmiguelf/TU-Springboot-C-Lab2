package com.example.gcashtrainingspringboot.controller;

import com.example.gcashtrainingspringboot.dto.ProductRequestDTO;
import com.example.gcashtrainingspringboot.dto.ProductResponseDTO;
import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.repository.ProductRepository;
import com.example.gcashtrainingspringboot.service.ProductService;
import com.example.gcashtrainingspringboot.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductResponseDTO> getAll(@RequestParam(required = false) String search, Pageable pageable){

        Page<Product> productsPage = productService.findAllProducts(search, pageable);

        return productsPage.map(product -> {
            ProductResponseDTO dto = new ProductResponseDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            return dto;
        });


    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable Long id){
        return productService.findProductByID(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());

        Product savedProduct = productService.saveProduct(product);

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(savedProduct.getId());
        productResponseDTO.setName(savedProduct.getName());
        productResponseDTO.setPrice(savedProduct.getPrice());

        return productResponseDTO;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        boolean deleted = productService.deleteProduct(id);

        if(deleted){
            return ResponseEntity.noContent().build(); // 204 No Content
        }else{
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // @PatchMapping
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> patchProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO){
        return productService.patch(id, productRequestDTO)
                .map(updated -> ResponseEntity.ok(toResponseDTO(updated)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // @PutMapping
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO){
        return productService.update(id, productRequestDTO)
                .map(updated -> ResponseEntity.ok(toResponseDTO(updated)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Utility
    private ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }
}
