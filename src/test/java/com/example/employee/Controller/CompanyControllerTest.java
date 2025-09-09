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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void should_return_all_companies_when_list_by_male() throws Exception {
        Company company = new Company(null,"alibaba");
        Company company1 = new Company(null ,"tencent");
        companyController.createCompany(company);
        companyController.createCompany(company1);
        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1));

    }
}
