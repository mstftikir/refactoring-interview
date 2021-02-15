package com.company.interview.controllers;

import com.company.interview.dtos.EmployeeDto;
import com.company.interview.entities.Employee;
import com.company.interview.mappers.EmployeeMapper;
import com.company.interview.services.EmployeeService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EmployeeController {

    private final EmployeeMapper employeeMapper = Mappers.getMapper(EmployeeMapper.class);

    public final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("v1/employees")
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeService.getEmployees(pageable);
    }

    @GetMapping("v1/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(value = "employeeId") Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @GetMapping("v1/offices/{officeId}/employees")
    public Page<Employee> getEmployeesByOfficeId(@PathVariable(value = "officeId") Long officeId, Pageable pageable) {
        return employeeService.getEmployeesByOfficeId(officeId, pageable);
    }

    @GetMapping("v1/offices/{officeId}/employees/{employeeId}")
    public Employee getEmployeeByOfficeId(@PathVariable(value = "officeId") Long officeId, @PathVariable(value = "employeeId") Long employeeId) {
        return employeeService.getEmployeeByOfficeId(officeId, employeeId);
    }

    @PostMapping("v1/offices/{officeId}/employees")
    public Employee saveEmployee(@PathVariable(value = "officeId") Long officeId, @Valid @RequestBody EmployeeDto newEmployeeDto) {
        Employee newEmployee = employeeMapper.toEntity(newEmployeeDto);
        return employeeService.saveEmployee(officeId, newEmployee);
    }

    @PutMapping("v1/offices/{officeId}/employees/{employeeId}")
    public Employee updateEmployee(@PathVariable(value = "officeId") Long officeId, @PathVariable(value = "employeeId") Long employeeId, @Valid @RequestBody EmployeeDto newEmployeeDto) {
        Employee newEmployee = employeeMapper.toEntity(newEmployeeDto);
        return employeeService.updateEmployee(officeId, employeeId, newEmployee);
    }

    @DeleteMapping("v1/offices/{officeId}/employees/{employeeId}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable(value = "officeId") Long officeId, @PathVariable(value = "employeeId") Long employeeId) {
        return employeeService.deleteEmployee(officeId, employeeId);
    }
}