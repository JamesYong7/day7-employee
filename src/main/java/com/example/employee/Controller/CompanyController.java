package com.example.employee.Controller;

import com.example.employee.Entity.Company;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("companies")
public class CompanyController {
    private final List<Company> companies = new ArrayList<>();
    private int id = 0;

    @PostMapping
    public void createCompany(@RequestBody Company company){
        int id = ++this.id;
        Company company1 = new Company(id, company.name());
        companies.add(company1);
    }

    @GetMapping
    public List<Company> getAllCompanies(){
        return companies;
    }

    public void clear(){
        companies.clear();
    }
}
