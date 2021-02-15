package com.company.interview.repositories;

import com.company.interview.entities.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends PagingAndSortingRepository<Office, Long> {
    Page<Office> findByCityId(Long cityId, Pageable pageable);
    Optional<Office> findByIdAndCityId(Long id, Long cityId);
}
