package com.company.interview.controllers;

import com.company.interview.entities.City;
import com.company.interview.entities.Country;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.CityRepository;
import com.company.interview.repositories.CountryRepository;
import com.company.interview.services.CityService;
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

public class CityControllerUnitTests {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    private City city;

    private List<City> cityList;

    private Country country;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        city = new City();
        city.setId(1L);
        city.setName("testCity");
        city.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        city.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        country = new Country();
        country.setId(1L);
        country.setName("testCountry");
        country.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        country.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        cityList = new LinkedList<>();

        for (int i = 0; i < 10 ; i++) {
            City newCity = new City();
            newCity.setId((long) i);
            newCity.setName("test" + i);
            newCity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newCity.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            newCity.setCountry(country);
            cityList.add(newCity);
        }
    }

    @Test
    public void getCitiesTest() {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<City> cities = new PageImpl<>(cityList, pageRequest, 10L);

        when(cityRepository.findAll(Mockito.any(Pageable.class))).thenReturn(cities);

        Page<City> citiesReturned = cityService.getCities(pageRequest);

        assertNotNull(citiesReturned);
        assertEquals(cityList.size() / pageSize, citiesReturned.getTotalPages());
        assertEquals(cityList.get(0), citiesReturned.getContent().get(0));
    }

    @Test
    public void getCityTest() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        City cityReturned = cityService.getCity(1L);

        assertNotNull(cityReturned);
        assertEquals(city, cityReturned);
    }

    @Test
    public void getCityThrowsExceptionTest() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        assertThrows(ResourceNotFoundException.class, () -> cityService.getCity(2L));
    }

    @Test
    public void getCitiesByCountryIdTest(){
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);
        Page<City> cities = new PageImpl<>(cityList, pageRequest, 10L);

        when(cityRepository.findByCountryId(1L, pageRequest)).thenReturn(cities);

        Page<City> citiesReturned = cityService.getCitiesByCountryId(1L, pageRequest);

        assertNotNull(citiesReturned);
        assertEquals(cityList.size() / pageSize, citiesReturned.getTotalPages());
        assertEquals(cityList.get(0), citiesReturned.getContent().get(0));
    }

    @Test
    public void getCityByCountryIdTest(){
        when(cityRepository.findByIdAndCountryId(10L, 1L)).thenReturn(Optional.of(city));

        City cityReturned = cityService.getCityByCountryId(1L, 10L);

        assertNotNull(cityReturned);
        assertEquals(city, cityReturned);
    }

    @Test
    public void getCityByCountryIdThrowsExceptionTest(){
        when(cityRepository.findByIdAndCountryId(1L, 10L)).thenReturn(Optional.of(city));

        assertThrows(ResourceNotFoundException.class, () -> cityService.getCityByCountryId(10L, 2L));
    }

    @Test
    public void saveCityTest(){
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(cityRepository.save(Mockito.any(City.class))).thenReturn(city);

        City cityReturned = cityService.saveCity(1L, city);

        assertNotNull(cityReturned);
        assertEquals(city, cityReturned);
    }

    @Test
    public void saveCityThrowsExceptionTest(){
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        assertThrows(ResourceNotFoundException.class, () -> cityService.saveCity(2L, city));
    }

    @Test
    public void updateCityTest(){
        when(countryRepository.existsById(1L)).thenReturn(true);
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(cityRepository.save(city)).thenReturn(city);

        City cityReturned = cityService.updateCity(1L, 10L, city);

        assertNotNull(cityReturned);
        assertEquals(city, cityReturned);
    }

    @Test
    public void updateCityNotFoundExceptionTest(){
        when(countryRepository.existsById(1L)).thenReturn(true);
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(cityRepository.save(city)).thenReturn(city);

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1L, 2L, city));
    }

    @Test
    public void updateCityCountryNotFoundExceptionTest(){
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(cityRepository.save(city)).thenReturn(city);

        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(1L, 10L, city));
    }

    @Test
    public void deleteCityTest(){
        when(cityRepository.findByIdAndCountryId(10L, 1L)).thenReturn(Optional.of(city));

        doNothing().when(cityRepository).delete(ArgumentMatchers.any(City.class));

        ResponseEntity<?> responseEntity = cityService.deleteCity(1L, 10L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteCityNotFoundExceptionTest(){
        when(cityRepository.findByIdAndCountryId(10L, 1L)).thenReturn(Optional.of(city));

        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(1L, 2L));
    }
}
