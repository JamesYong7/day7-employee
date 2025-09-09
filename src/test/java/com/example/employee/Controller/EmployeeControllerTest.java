package com.example.employee.Controller;

import com.example.employee.Entity.Employee;
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
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;

    @Test
     void should_return_new_employee_when_post() throws Exception {
        //given
        String requestBody = """
                {
                    "name": "John Doe",
                    "age": 30,
                    "gender" : "Male",
                    "salary": 5000
                    }""";
        MockHttpServletRequestBuilder request = post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        //when & then
        mockMvc.perform(request).andExpect(status ().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000));

    }

    @Test
    void should_return_employees_when_get_employee_with_id_exist() throws Exception {
        Employee employee = new Employee(1,"John Doe","male",30,5000);
        Employee employee1 = employeeController.createEmployee(employee);
        MockHttpServletRequestBuilder request = get("/employees/" + employee1.id())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(employee1.id()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.salary").value(5000));
    }
}
