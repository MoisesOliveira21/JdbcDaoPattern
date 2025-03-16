package org.example.dao;

import org.example.dao.impl.ProductDaoJDBC;

public class DaoFactory {

    public static ProductDao createProductDao(){
        return new ProductDaoJDBC();
    }
}
