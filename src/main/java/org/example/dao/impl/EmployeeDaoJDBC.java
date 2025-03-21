package org.example.dao.impl;

import org.example.dao.EmployeeDao;
import org.example.entities.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EmployeeDaoJDBC implements EmployeeDao {




    @Override
    public void insert(Employee obj) {

    }

    @Override
    public void update(Employee obj) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Employee findById(int id) {

   return null;

    }

    @Override
    public List<Employee> findAll() {
        return null;
    }
}
