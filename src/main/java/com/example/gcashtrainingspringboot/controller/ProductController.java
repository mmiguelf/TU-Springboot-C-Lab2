package com.example.gcashtrainingspringboot.controller;

import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAll(){
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable Long id){
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product newProduct){
        return productRepository.save(newProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        boolean deleted = productRepository.deleteByID(id);

        if(deleted){
            return ResponseEntity.noContent().build(); // 204 No Content
        }else{
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // @PatchMapping
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @Valid @RequestBody Product patchData){
        Optional<Product> existingOpt = productRepository.findById(id);

        if(existingOpt.isPresent()){
            Product existing = existingOpt.get();

            // Update only the non-null fields
            if (patchData.getName() != null) {
                existing.setName(patchData.getName());
            }

            if (patchData.getPrice() != null) {
                existing.setPrice(patchData.getPrice());
            }

            Product saved = productRepository.save(existing);
            return ResponseEntity.ok(saved);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    // @PutMapping
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct){
        Optional<Product> existing = productRepository.findById(id);

        if(existing.isPresent()){
            updatedProduct.setId(id);
            Product saved = productRepository.save(updatedProduct);
            return ResponseEntity.ok(saved);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
