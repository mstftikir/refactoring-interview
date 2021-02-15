package com.company.interview.repositories;

import com.company.interview.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Long> {
    Page<Employee> findByOfficeId(Long officeId, Pageable pageable);
    Optional<Employee> findByIdAndOfficeId(Long id, Long officeId);
}
