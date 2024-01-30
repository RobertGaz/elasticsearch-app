package my.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.model.Employee;
import my.service.EmployeeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employees API V2")
@RestController
@RequestMapping("/api/v2/employees")
public class EmployeeControllerV2 {

    private EmployeeService employeeService;

    public EmployeeControllerV2(@Qualifier("employeeServiceJavaClientImpl") EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(description = "Get all employees")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAll() {
        return employeeService.getAllEmployees();
    }

    @Operation(description = "Get employee by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @Operation(description = "Create employee")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @Operation(description = "Delete employee by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        employeeService.deleteEmployeeById(id);
    }

    @Operation(description = "Search employees")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> search(@RequestParam String field,
                                 @RequestParam String value) {
        return employeeService.search(field, value);
    }

    @Operation(description = "Get aggregation")
    @GetMapping("/aggregate")
    @ResponseStatus(HttpStatus.OK)
    public String aggregate(@RequestParam String aggregationField,
                          @RequestParam String metricType,
                          @RequestParam String metricField) {
        return employeeService.aggregate(aggregationField, metricType, metricField);
    }

}
