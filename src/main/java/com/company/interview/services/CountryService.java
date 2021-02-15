package com.company.interview.services;

import com.company.interview.entities.Country;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.company.interview.commons.CommonMessages.COUNTRY_NOT_FOUND;

@Service
public class CountryService {

    public final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Page<Country> getCountries(Pageable pageable) {
        return countryRepository.findAll(pageable);
    }

    public Country getCountry(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> new ResourceNotFoundException(COUNTRY_NOT_FOUND.replace("{0}", countryId.toString())));
    }

    public Country saveCountry(Country newCountry) {
        return countryRepository.save(newCountry);
    }

    public Country updateCountry(Country newCountry, Long countryId) {
        return countryRepository.findById(countryId)
                .map(country -> {
                    country.setName(newCountry.getName());
                    return countryRepository.save(country);
                }).orElseThrow(() -> new ResourceNotFoundException(COUNTRY_NOT_FOUND.replace("{0}", countryId.toString())));
    }

    public ResponseEntity<Object> deleteCountry(Long countryId) {
        return countryRepository.findById(countryId).map(country -> {
            countryRepository.delete(country);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(COUNTRY_NOT_FOUND.replace("{0}", countryId.toString())));
    }
}
