package com.company.interview.controllers;

import com.company.interview.dtos.CountryDto;
import com.company.interview.entities.Country;
import com.company.interview.mappers.CountryMapper;
import com.company.interview.services.CountryService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CountryController {

    private final CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    public final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("v1/countries")
    public Page<Country> getCountries(Pageable pageable) {
        return countryService.getCountries(pageable);
    }

    @GetMapping("v1/countries/{countryId}")
    public Country getCountry(@PathVariable Long countryId) {
        return countryService.getCountry(countryId);
    }

    @PostMapping("v1/countries")
    public Country saveCountry(@Valid @RequestBody CountryDto newCountryDto) {
        Country newCountry = countryMapper.toEntity(newCountryDto);
        return countryService.saveCountry(newCountry);
    }

    @PutMapping("v1/countries/{countryId}")
    public Country updateCountry(@RequestBody CountryDto newCountryDto, @PathVariable Long countryId) {
        Country newCountry = countryMapper.toEntity(newCountryDto);
        return countryService.updateCountry(newCountry, countryId);
    }

    @DeleteMapping("v1/countries/{countryId}")
    public ResponseEntity<Object> deleteCountry(@PathVariable Long countryId) {
        return countryService.deleteCountry(countryId);
    }
}
