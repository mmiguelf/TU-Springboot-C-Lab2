package com.example.gcashtrainingspringboot.repository;

import com.example.gcashtrainingspringboot.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository{
    // In-memory database
    private final List<Product> productList = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong nextId = new AtomicLong(); // Simple Increment

    public ProductRepository(){
        productList.add(new Product(nextId.getAndIncrement(), "Laptop", 30000.00));
        productList.add(new Product(nextId.getAndIncrement(), "Mouse", 1000.00));
        productList.add(new Product(nextId.getAndIncrement(), "Keyboard", 1500.00));
        productList.add(new Product(nextId.getAndIncrement(), "Laptop Stand", 500.00));

        System.out.println("Product Repository initialized with" + productList.size() + "products");
    }

    public List<Product> findAll(){
        return new ArrayList<>(productList);
    }

    public Optional<Product> findById(Long id){
        return productList.stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    public Product save(Product product){
        if(product.getId() == null){
            product.setId(nextId.getAndIncrement());
            productList.add(product);
            System.out.println("Product saved (new): " + product);
            return product;
        }else{
            for(int i = 0; i < productList.size(); i++){
                if(productList.get(i).getId().equals(product.getId())){
                    productList.set(i, product);
                    System.out.println("Product Updated: " + product);
                    return product;
                }
            }
            System.out.println("Product with ID " + product.getId());
            return product;
        }
    }

    public boolean deleteByID(Long id){
        return productList.removeIf(product -> product.getId().equals(id));
    }
}
