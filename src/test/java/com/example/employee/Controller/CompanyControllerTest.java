package com.example.employee.Controller;

import com.example.employee.Entity.Company;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyController companyController;

    @AfterEach
    public void clean(){
        companyController.clear();
    }

    @Test
    void should_return_all_companies_when_list() throws Exception {
        Company company = new Company(null,"alibaba");
        companyController.createCompany(company);
        String requestBody = """
                {
                    "name": "alibaba"
                    }""";
        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));

    }

    @Test
    void should_return_company_when_get_company_with_id_exist() throws Exception {
        Company company = new Company(null,"alibaba");
        Company company1 = new Company(null ,"tencent");
        companyController.createCompany(company);
        companyController.createCompany(company1);
        MockHttpServletRequestBuilder request = get("/companies/" + 1)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("alibaba"));
    }

    @Test
    void should_show_by_page() throws Exception {
        Company company = new Company(null,"alibaba");
        Company company1 = new Company(null ,"tencent");
        Company company2 = new Company(null ,"baidu");
        Company company3 = new Company(null ,"jd");
        Company company4 = new Company(null ,"meituan");
        companyController.createCompany(company);
        companyController.createCompany(company1);
        companyController.createCompany(company2);
        companyController.createCompany(company3);
        companyController.createCompany(company4);
        MockHttpServletRequestBuilder request = get("/companies?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_update_name_when_put_company_with_id_exist() throws Exception {
        Company company = new Company(null,"alibaba");
        companyController.createCompany(company);
        String requestBody = """
                {
                    "name": "alibaba"
                    }""";
        MockHttpServletRequestBuilder request = put("/companies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("alibaba"));
    }

    @Test
    void should_delete_company_when_delete_company_with_id_exist() throws Exception {
        Company company = new Company(null,"alibaba");
        Company company1 = new Company(null ,"tencent");
        companyController.createCompany(company);
        companyController.createCompany(company1);
        MockHttpServletRequestBuilder request = delete("/companies/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
