package org.example.dao.impl;

import org.example.dao.EmployeeDao;
import org.example.db.DB;
import org.example.entities.Employee;
import org.example.exception.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoJDBC implements EmployeeDao {




    private Connection conn;

    public EmployeeDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Employee obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "INSERT INTO employee (name, role, email, phoneNumber, hireDate) " +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getRole());
            st.setString(3, obj.getEmail());
            st.setString(4, obj.getPhoneNumber());
            st.setDate(5, obj.getHireDate());

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
    public void update(Employee obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE employee SET name = ?, role = ?, email = ?, phoneNumber = ?, hireDate = ? " +
                            "WHERE id = ?");

            st.setString(1, obj.getName());
            st.setString(2, obj.getRole());
            st.setString(3, obj.getEmail());
            st.setString(4, obj.getPhoneNumber());
            st.setDate(5, obj.getHireDate());
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

        try {
            st = conn.prepareStatement("DELETE FROM employee WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Employee findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM employee WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateEmployee(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Employee> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Employee> employeeList = new ArrayList<>();

        try {
            st = conn.prepareStatement("SELECT * FROM employee ORDER BY name");
            rs = st.executeQuery();

            while (rs.next()) {
                employeeList.add(instantiateEmployee(rs));
            }

            return employeeList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Employee instantiateEmployee(ResultSet rs) throws SQLException {
        Employee emp = new Employee();

        emp.setId(rs.getInt("id"));
        emp.setName(rs.getString("name"));
        emp.setRole(rs.getString("role"));
        emp.setEmail(rs.getString("email"));
        emp.setPhoneNumber(rs.getString("phoneNumber"));
        emp.setHireDate(rs.getDate("hireDate"));

        return emp;
    }
}
