package org.example.dao.impl;

import org.example.dao.ProductDao;
import org.example.db.DB;
import org.example.entities.Employee;
import org.example.entities.Product;
import org.example.exception.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private Connection conn;

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product obj) {
        PreparedStatement st = null;

        try {

            st = conn.prepareStatement(
                    "INSERT INTO product"
                            + "(name, description, price, quantityInStock, category, employee_id)" +
                            "VALUES"
                            + "(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getDescription());
            st.setDouble(3, obj.getPrice());
            st.setInt(4, obj.getQuantityInStock());
            st.setString(5, obj.getCategory());
            st.setInt(6, obj.getEmployee().getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);

        }

    }

    @Override
    public void update(Product obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE Product " +
                    "SET name = ?, description = ?, price = ?, quantityInStock = ?, category = ? " +
                    "WHERE id = ?");

            st.setString(1, obj.getName());
            st.setString(2, obj.getDescription());
            st.setDouble(3, obj.getPrice());
            st.setInt(4, obj.getQuantityInStock());
            st.setString(5, obj.getCategory());
            st.setInt(6, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public void deleteById(int id) {
        PreparedStatement st = null;

        try{

            st = conn.prepareStatement("DELETE FROM product where id = ?");

            st.setInt(1,id);

            st.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }

    }

    @Override
    public List<Product> findByEmployee(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Product> productList = new ArrayList<>();

        try {

            st = conn.prepareStatement("select product.*,employee.name as EmpName, " +
                    "employee.role,employee.email,employee.phoneNumber, employee.hireDate" +
                    " from product Inner join employee on product.employee_id = employee.id" +
                    " where Employee_id = ?\n" +
                    "order by name");

            st.setInt(1, id);
            rs = st.executeQuery();

            Employee emp;
            if (rs.next()) {
                emp = instantiateEmployee(rs);
                productList.add(instantiateProduct(rs, emp));
                while (rs.next()) {
                    productList.add(instantiateProduct(rs, emp));
                }
            }
            return productList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Product findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT product.*, " +
                            "employee.id AS employee_id, employee.name AS empName, " +
                            "employee.role, employee.email, employee.phoneNumber, employee.hireDate " +
                            "FROM product " +
                            "INNER JOIN employee ON product.employee_id = employee.id " +
                            "WHERE product.id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Employee emp = instantiateEmployee(rs);

                return instantiateProduct(rs, emp);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }


    }

    private Product instantiateProduct(ResultSet rs, Employee emp) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantityInStock(rs.getInt("quantityInStock"));
        product.setCategory(rs.getString("category"));
        product.setEmployee(emp);
        return product;
    }

    private Employee instantiateEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();

        emp.setId(rs.getInt("employee_id"));
        emp.setName((rs.getString("name")));
        emp.setRole(rs.getString("role"));
        emp.setEmail(rs.getString("email"));
        emp.setPhoneNumber(rs.getString("phoneNumber"));
        emp.setHireDate(rs.getDate("hireDate"));
        return emp;
    }

    @Override
    public List<Product> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Product> productList = new ArrayList<>();

        try {

            st = conn.prepareStatement("select product.*,employee.name as EmpName,\n" +
                    "\t\temployee.role,employee.email,employee.phoneNumber, employee.hireDate\n" +
                    "                     from product Inner join employee on product.employee_id = employee.id\n" +
                    "                    order by name");

            rs = st.executeQuery();

            Employee emp;
            if (rs.next()) {
                emp = instantiateEmployee(rs);
                productList.add(instantiateProduct(rs, emp));
                while (rs.next()) {
                    productList.add(instantiateProduct(rs, emp));
                }
            }
            return productList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
