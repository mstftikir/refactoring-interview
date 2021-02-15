package com.company.interview.controllers;

import com.company.interview.entities.Employee;
import com.company.interview.entities.Office;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.EmployeeRepository;
import com.company.interview.repositories.OfficeRepository;
import com.company.interview.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class EmployeeControllerUnitTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    private List<Employee> employeeList;

    private Office office;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("testEmployee");
        employee.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        employee.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        office = new Office();
        office.setId(1L);
        office.setName("testOffice");
        office.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        office.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        employeeList = new LinkedList<>();

        for (int i = 0; i < 10 ; i++) {
            Employee newEmployee = new Employee();
            newEmployee.setId((long) i);
            newEmployee.setName("test" + i);
            newEmployee.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newEmployee.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            newEmployee.setOffice(office);
            employeeList.add(newEmployee);
        }
    }

    @Test
    public void getEmployeesTest() {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<Employee> employees = new PageImpl<>(employeeList, pageRequest, 10L);

        when(employeeRepository.findAll(Mockito.any(Pageable.class))).thenReturn(employees);

        Page<Employee> employeesReturned = employeeService.getEmployees(pageRequest);

        assertNotNull(employeesReturned);
        assertEquals(employeeList.size() / pageSize, employeesReturned.getTotalPages());
        assertEquals(employeeList.get(0), employeesReturned.getContent().get(0));
    }

    @Test
    public void getEmployeeTest() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee employeeReturned = employeeService.getEmployee(1L);

        assertNotNull(employeeReturned);
        assertEquals(employee, employeeReturned);
    }

    @Test
    public void getEmployeeThrowsExceptionTest() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployee(2L));
    }

    @Test
    public void getEmployeesByOfficeIdTest(){
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);
        Page<Employee> employees = new PageImpl<>(employeeList, pageRequest, 10L);

        when(employeeRepository.findByOfficeId(1L, pageRequest)).thenReturn(employees);

        Page<Employee> employeesReturned = employeeService.getEmployeesByOfficeId(1L, pageRequest);

        assertNotNull(employeesReturned);
        assertEquals(employeeList.size() / pageSize, employeesReturned.getTotalPages());
        assertEquals(employeeList.get(0), employeesReturned.getContent().get(0));
    }

    @Test
    public void getEmployeeByOfficeIdTest(){
        when(employeeRepository.findByIdAndOfficeId(10L, 1L)).thenReturn(Optional.of(employee));

        Employee employeeReturned = employeeService.getEmployeeByOfficeId(1L, 10L);

        assertNotNull(employeeReturned);
        assertEquals(employee, employeeReturned);
    }

    @Test
    public void findByIdAndOfficeId(){
        when(employeeRepository.findByIdAndOfficeId(10L, 1L)).thenReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeByOfficeId(10L, 2L));
    }

    @Test
    public void saveEmployeeTest(){
        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);

        Employee employeeReturned = employeeService.saveEmployee(1L, employee);

        assertNotNull(employeeReturned);
        assertEquals(employee, employeeReturned);
    }

    @Test
    public void saveEmployeeThrowsExceptionTest(){
        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(2L, employee));
    }

    @Test
    public void updateEmployeeTest(){
        when(officeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));

        Employee employeeReturned = employeeService.updateEmployee(1L, 10L, employee);

        assertNotNull(employeeReturned);
        assertEquals(employee, employeeReturned);
    }

    @Test
    public void updateEmployeeNotFoundExceptionTest(){
        when(officeRepository.existsById(1L)).thenReturn(true);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(1L, 2L, employee));
    }

    @Test
    public void updateEmployeeOfficeNotFoundExceptionTest(){
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeRepository.findById(10L)).thenReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(1L, 2L, employee));
    }

    @Test
    public void deleteEmployeeTest(){
        when(employeeRepository.findByIdAndOfficeId(10L, 1L)).thenReturn(Optional.of(employee));

        doNothing().when(employeeRepository).delete(ArgumentMatchers.any(Employee.class));

        ResponseEntity<?> responseEntity = employeeService.deleteEmployee(1L, 10L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteEmployeeNotFoundExceptionTest(){
        when(employeeRepository.findByIdAndOfficeId(10L, 1L)).thenReturn(Optional.of(employee));

        assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(1L, 2L));
    }
}
