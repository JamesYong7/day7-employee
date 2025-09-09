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
    public List<Company> getAllCompanies(@RequestParam(required = false, value = "page") Integer page,
                                         @RequestParam (required = false, value = "size") Integer size){
        if(page == null && size == null){
            page = 0;
            size = 0;
        }
        if(page != 0 && size != 0){
            int start = (page - 1) * size;
            int end = Math.min(start + size, companies.size());
            if (start >= companies.size() || start < 0) {
                return new ArrayList<>();
            }
            return companies.subList(start, end);
        }
        return companies;
    }

    @GetMapping("{id}")
    public Company getCompanyById(@PathVariable int id){
        return companies.stream().filter(company -> company.id() == id).findFirst().orElse(null);
    }

    @PutMapping("{id}")
    public Company changeCompanyName(@PathVariable int id ,@RequestBody Company companyRequest) {
        for (Company company : companies) {
            if(company.id().equals(id)){
                Company newCompany = new Company(id, companyRequest.name());
                companies.remove(company);
                companies.add(newCompany);
                return newCompany;
            }
        }
        return null;
    }

    @DeleteMapping("{id}")
    public List<Company> deleteCompanyById(@PathVariable int id){
        companies.removeIf(company -> company.id().equals(id));
        return companies;
    }

    public void clear(){
        companies.clear();
        id = 0;
    }
}
