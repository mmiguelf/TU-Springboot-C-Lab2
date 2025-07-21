package com.example.gcashtrainingspringboot.service;

import com.example.gcashtrainingspringboot.dto.ProductRequestDTO;
import com.example.gcashtrainingspringboot.model.Product;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Page<Product> findAllProducts(String search, Pageable pageable);
    Optional<Product> findProductByID(Long id);
    Product saveProduct(Product product);
    Optional<Product> update(Long id, ProductRequestDTO newProduct);
    Optional<Product> patch(Long id, ProductRequestDTO patchProduct);
    boolean deleteProduct(Long id);
}
