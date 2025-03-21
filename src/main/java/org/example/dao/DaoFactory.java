package org.example.dao;

import org.example.dao.impl.ProductDaoJDBC;
import org.example.db.DB;

public class DaoFactory {

    public static ProductDao createProductDao(){
        return new ProductDaoJDBC(DB.getConnection());
    }
}
