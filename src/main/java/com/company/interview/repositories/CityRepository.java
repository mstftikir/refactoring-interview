package com.company.interview.repositories;

import com.company.interview.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends PagingAndSortingRepository<City, Long> {
    Page<City> findByCountryId(Long countryId, Pageable pageable);
    Optional<City> findByIdAndCountryId(Long id, Long countryId);
}
