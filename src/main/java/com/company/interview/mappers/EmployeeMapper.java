package com.company.interview.mappers;

import com.company.interview.dtos.EmployeeDto;
import com.company.interview.entities.Employee;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {
    Employee toEntity(EmployeeDto employeeDto);
}
