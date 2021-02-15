package com.company.interview.controllers;

import com.company.interview.entities.Country;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.CountryRepository;
import com.company.interview.services.CountryService;
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

public class CountryServiceUnitTests {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country country;

    private List<Country> countryList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        country = new Country();
        country.setId(1L);
        country.setName("testCountry");
        country.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        country.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        countryList = new LinkedList<>();

        for (int i = 0; i < 10 ; i++) {
            Country newCountry = new Country();
            newCountry.setId((long) i);
            newCountry.setName("test" + i);
            newCountry.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newCountry.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            countryList.add(newCountry);
        }
    }

    @Test
    public void getCountriesTest() {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<Country> countries = new PageImpl<>(countryList, pageRequest, 10L);

        when(countryRepository.findAll(Mockito.any(Pageable.class))).thenReturn(countries);

        Page<Country> countriesReturned = countryService.getCountries(pageRequest);

        assertNotNull(countriesReturned);
        assertEquals(countryList.size() / pageSize, countriesReturned.getTotalPages());
        assertEquals(countryList.get(0), countriesReturned.getContent().get(0));
    }

    @Test
    public void getCountryTest() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        Country countryReturned = countryService.getCountry(1L);

        assertNotNull(countryReturned);
        assertEquals(country, countryReturned);
    }

    @Test
    public void getCountryThrowsExceptionTest() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        assertThrows(ResourceNotFoundException.class, () -> countryService.getCountry(2L));
    }

    @Test
    public void saveCountryTest() {
        when(countryRepository.save(Mockito.any(Country.class))).thenReturn(country);

        Country countryReturned = countryService.saveCountry(country);

        assertNotNull(countryReturned);
        assertEquals(country, countryReturned);
    }

    @Test
    public void updateCountryTest() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(countryRepository.save(country)).thenReturn(country);

        Country countryReturned = countryService.updateCountry(country, 1L);

        assertNotNull(countryReturned);
        assertEquals(country, countryReturned);
    }

    @Test
    public void updateCountryThrowsExceptionTest() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        assertThrows(ResourceNotFoundException.class, () -> countryService.updateCountry(country, 2L));
    }

    @Test
    public void deleteCountryTest() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        doNothing().when(countryRepository).delete(ArgumentMatchers.any(Country.class));

        ResponseEntity<?> responseEntity = countryService.deleteCountry(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteCountryThrowsExceptionTest() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        assertThrows(ResourceNotFoundException.class, () -> countryService.deleteCountry(2L));
    }
}
