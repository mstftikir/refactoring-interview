package com.company.interview.integrationtests.controllers;

import com.company.interview.dtos.EmployeeDto;
import com.company.interview.entities.Employee;
import com.company.interview.entities.Office;
import com.company.interview.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private List<Employee> employeeList;

    @BeforeEach
    void setup() {
        employeeList = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            Employee newEmployee = new Employee();
            newEmployee.setId((long) i);
            newEmployee.setName("testEmployee" + i);
            newEmployee.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newEmployee.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            employeeList.add(newEmployee);
        }
    }

    @Test
    void getEmployeesShouldReturnAnExpectedEmployee() throws Exception {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<Employee> employees = new PageImpl<>(employeeList, pageRequest, 10L);

        when(employeeService.getEmployees(Mockito.any(Pageable.class))).thenReturn(employees);

        mockMvc.perform(get("/v1/employees")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("testEmployee")));
    }

    @Test
    void saveEmployeeShouldReturnOk() throws Exception {
        Office office = new Office();
        office.setId(1L);
        office.setName("testOffice");
        office.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        office.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("testEmployee");
        employee.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        employee.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        employee.setOffice(office);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("testEmployeeDto");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(employeeDto);

        when(employeeService.saveEmployee(Mockito.any(Long.class), Mockito.any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/v1/offices/1/employees").contentType(APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isOk());
    }
}
