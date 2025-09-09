package com.example.employee.Controller;

import com.example.employee.Entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee employee1 = new Employee(id, employee.name(),employee.gender(),employee.age(),employee.salary());
        employees.add(employee1);
        return employee1;
    }
}
