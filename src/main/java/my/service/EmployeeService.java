package my.service;

import my.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    Employee getEmployeeById(String id);

    String createEmployee(Employee employee);

    void deleteEmployeeById(String id);

    List<Employee> search(String field, String value);

    String aggregate(String aggregationField, String metricType, String metricField);
}
