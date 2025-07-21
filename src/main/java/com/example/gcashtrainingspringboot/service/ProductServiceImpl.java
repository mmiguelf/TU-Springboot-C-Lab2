package com.example.gcashtrainingspringboot.service;

import com.example.gcashtrainingspringboot.dto.ProductRequestDTO;
import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAllProducts(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    @Override
    public Optional<Product> findProductByID(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> update(Long id, ProductRequestDTO newProduct) {
        Optional<Product> existing = productRepository.findById(id);
        Product update = new Product();
        update.setName(newProduct.getName());
        update.setPrice(newProduct.getPrice());
        if (existing.isPresent()){
            update.setId(id);
            return Optional.of(productRepository.save(update));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> patch(Long id, ProductRequestDTO patchProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if ((existing.isPresent())){
            Product exist = existing.get();

            if(patchProduct.getName() != null){
                exist.setName(patchProduct.getName());
            }

            if(patchProduct.getPrice() != null){
                exist.setPrice(patchProduct.getPrice());
            }

            return Optional.of(productRepository.save(exist));
        }


        return Optional.empty();
    }

    @Override
    public boolean deleteProduct(Long id) {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;

    }
}
