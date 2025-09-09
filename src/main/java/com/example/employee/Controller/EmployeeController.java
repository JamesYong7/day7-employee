package com.example.employee.Controller;

import com.example.employee.Entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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
        return employees.stream().filter(employee -> employee.id() == id).findFirst().orElse(null);
    }

    @GetMapping
    public List<Employee> getMaleEmployees(@RequestParam (required = false, value = "gender") String gender,
                                           @RequestParam(required = false, value = "page") Integer page, @RequestParam (required = false, value = "size") Integer size) {
        if(page == null && size == null){
            page = 0;
            size = 0;
        }
        if (gender != null ) {
            return employees.stream()
                    .filter(employee -> employee.gender().trim().equalsIgnoreCase(gender.trim()))
                    .toList();
        }
        if(page != 0 && size != 0){
            int start = (page - 1) * size;
            int end = Math.min(start + size, employees.size());
            if (start >= employees.size() || start < 0) {
                return new ArrayList<>();
            }
            return employees.subList(start, end);
        }
        return employees;
    }

    @PutMapping("{id}")
    public Employee changeEmployeeSalary(@PathVariable int id ,@RequestBody Employee employeeRequest) {
        for (Employee employee : employees) {
            if(employee.id().equals(id)){
                Employee newEmployee = new Employee(id, employeeRequest.name(), employeeRequest.gender(), employeeRequest.age(), employeeRequest.salary());
                employees.remove(employee);
                employees.add(newEmployee);
                return newEmployee;
            }
        }
        return null;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable int id) {
        employees.removeIf(employee -> employee.id().equals(id));
//        if (!removed) {
//            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Employee not found");
//        }
    }

    public void clear(){
        employees.clear();
        id = 0;
    }
}
