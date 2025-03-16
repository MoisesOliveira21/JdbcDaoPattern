package org.example.dao;

import org.example.entities.Employee;

import java.util.List;

public interface EmployeeDao {
    void insert(Employee obj);
    void update(Employee obj);
    void deleteById(int id);
    Employee findById(int id);
    List<Employee> findAll();
}
