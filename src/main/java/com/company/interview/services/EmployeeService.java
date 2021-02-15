package com.company.interview.services;

import com.company.interview.entities.Employee;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.EmployeeRepository;
import com.company.interview.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.company.interview.commons.CommonMessages.*;

@Service
public class EmployeeService {

    public final EmployeeRepository employeeRepository;

    public final OfficeRepository officeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, OfficeRepository officeRepository) {
        this.employeeRepository = employeeRepository;
        this.officeRepository = officeRepository;
    }

    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND.replace("{0}", employeeId.toString())));
    }

    public Page<Employee> getEmployeesByOfficeId(Long officeId, Pageable pageable) {
        return employeeRepository.findByOfficeId(officeId, pageable);
    }

    public Employee getEmployeeByOfficeId(Long officeId, Long employeeId) {
        return employeeRepository.findByIdAndOfficeId(employeeId, officeId).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_AND_OFFICE_NOT_FOUND.replace("{0}", employeeId.toString()).replace("{1}", officeId.toString())));
    }

    public Employee saveEmployee(Long officeId, Employee newEmployee) {
        return officeRepository.findById(officeId).map(office -> {
            newEmployee.setOffice(office);
            return employeeRepository.save(newEmployee);
        }).orElseThrow(() -> new ResourceNotFoundException(OFFICE_NOT_FOUND.replace("{0}", officeId.toString())));
    }

    public Employee updateEmployee(Long officeId, Long employeeId, Employee newEmployee) {
        if (!officeRepository.existsById(officeId)) {
            throw new ResourceNotFoundException(OFFICE_NOT_FOUND.replace("{0}", officeId.toString()));
        }

        return employeeRepository.findById(employeeId).map(employee -> {
            employee.setName(newEmployee.getName());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_NOT_FOUND.replace("{0}", employeeId.toString())));
    }

    public ResponseEntity<Object> deleteEmployee(Long officeId, Long employeeId) {
        return employeeRepository.findByIdAndOfficeId(employeeId, officeId).map(employee -> {
            employeeRepository.delete(employee);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_AND_OFFICE_NOT_FOUND.replace("{0}", employeeId.toString()).replace("{1}", officeId.toString())));
    }
}
