package org.example.dao;

import org.example.entities.Product;

import java.util.List;

public interface ProductDao {
    void insert(Product obj);
    void update(Product obj);
    void deleteById(int id);
    Product findById(int id);
    List<Product> findAll();

    List<Product> findByEmployee(int id);
}
