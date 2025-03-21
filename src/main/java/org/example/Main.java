package org.example;

import org.example.dao.DaoFactory;
import org.example.dao.ProductDao;
import org.example.entities.Employee;
import org.example.entities.Product;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ProductDao productDao = DaoFactory.createProductDao();
        Product product = productDao.findById(1);
        product.setName("Notebook Gamer");
        product.setDescription("aaaaaaa");
        product.setPrice(7500.00);
        product.setQuantityInStock(5);
        product.setCategory("Eletrônicos");
        productDao.update(product);

        productDao.deleteById(1);
        System.out.println("Produto atualizado com sucesso!");
    }
}
