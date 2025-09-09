package com.example.employee.Controller;

import com.example.employee.Entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("employees")
public class EmployeeController {
    private final List<Employee> employees = new ArrayList<>();
    private int id = 0;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee employee1 = new Employee(id, employee.name(),employee.gender(),employee.age(),employee.salary());
        employees.add(employee1);
        return employee1;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeById(@PathVariable int id) {
        for (Employee employee : employees) {
            if (employee.id().equals(id)) {
                return employee;
            }
        }
        return null;
//        return employees.stream().filter(employee -> employee.id() == id).findFirst().orElse(null);
    }

    @GetMapping
    public List<Employee> getMaleEmployees(@RequestParam (required = false) String gender) {
        return employees.stream()
                .filter(employee -> Objects.equals(employee.gender().toLowerCase(), gender.toLowerCase()))
                .toList();

    }

    public void clear(){
        employees.clear();
        id = 0;
    }
}
