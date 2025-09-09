package com.example.employee.Controller;

import com.example.employee.Entity.Employee;
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
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;

    @AfterEach
    public void clean(){
        employeeController.clear();
    }

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
        Employee employee = new Employee(null,"John Doe","male",30,5000);
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

    @Test
    void should_return_males_when_list_by_male() throws Exception {
        employeeController.createEmployee(new Employee(null,"John Doe","male",30,5000));
        employeeController.createEmployee(new Employee(null,"Alice","female",30,5000));
        MockHttpServletRequestBuilder request = get("/employees?gender=male")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].age").value(30))
                .andExpect(jsonPath("$[0].gender").value("male"))
                .andExpect(jsonPath("$[0].salary").value(5000));
    }

    @Test
    void should_return_all_employees_when_list() throws Exception {
        employeeController.createEmployee(new Employee(null,"John Doe","male",30,5000));
        employeeController.createEmployee(new Employee(null,"Alice","female",30,5000));
        employeeController.createEmployee(new Employee(null,"Ben","male",30,5000));

        MockHttpServletRequestBuilder request = get("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void should_update_salary_when_employee_exists() throws Exception {
        //given
        employeeController.createEmployee(new Employee(null,"John Doe","male",30,5000));
        employeeController.createEmployee(new Employee(null,"Alice","female",30,5000));
        employeeController.createEmployee(new Employee(null,"Ben","male",30,5000));
        String requestBody = """
                {
                    "name": "John Doe",
                    "age": 30,
                    "gender" : "Male",
                    "salary": 50000
                    }""";

        MockHttpServletRequestBuilder request = put("/employees/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody);

        mockMvc.perform(request).andExpect(status ().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.salary").value(50000));
    }

    @Test
    void should_return_error_204_message_when_delete_null_employee() throws Exception {
        employeeController.createEmployee(new Employee(1,"John Doe","male",30,5000));
        employeeController.createEmployee(new Employee(2,"Alice","female",30,5000));
        employeeController.createEmployee(new Employee(3,"Ben","male",30,5000));

        MockHttpServletRequestBuilder request = delete("/employees/5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status ().isNoContent());
    }

    @Test
    void should_show_by_page() throws Exception {
        employeeController.createEmployee(new Employee(null, "John Doe", "male", 30, 5000));
        employeeController.createEmployee(new Employee(null, "Alice", "female", 30, 5000));
        employeeController.createEmployee(new Employee(null, "Ben", "male", 30, 5000));
        employeeController.createEmployee(new Employee(null, "C", "male", 30, 5000));
        employeeController.createEmployee(new Employee(null, "D", "male", 30, 5000));
        MockHttpServletRequestBuilder request = get("/employees?page=1&size=2")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
