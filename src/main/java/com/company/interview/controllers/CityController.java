package com.company.interview.controllers;

import com.company.interview.dtos.CityDto;
import com.company.interview.entities.City;
import com.company.interview.mappers.CityMapper;
import com.company.interview.services.CityService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CityController {

    private final CityMapper cityMapper = Mappers.getMapper(CityMapper.class);

    public final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("v1/cities")
    public Page<City> getCities(Pageable pageable) {
        return cityService.getCities(pageable);
    }

    @GetMapping("v1/cities/{cityId}")
    public City getCity(@PathVariable(value = "cityId") Long cityId) {
        return cityService.getCity(cityId);
    }

    @GetMapping("v1/countries/{countryId}/cities")
    public Page<City> getCitiesByCountryId(@PathVariable(value = "countryId") Long countryId, Pageable pageable) {
        return cityService.getCitiesByCountryId(countryId, pageable);
    }

    @GetMapping("v1/countries/{countryId}/cities/{cityId}")
    public City getCityByCountryId(@PathVariable(value = "countryId") Long countryId, @PathVariable(value = "cityId") Long cityId) {
        return cityService.getCityByCountryId(cityId, countryId);
    }

    @PostMapping("v1/countries/{countryId}/cities")
    public City saveCity(@PathVariable(value = "countryId") Long countryId, @Valid @RequestBody CityDto newCityDto) {
        City newCity = cityMapper.toEntity(newCityDto);
        return cityService.saveCity(countryId, newCity);
    }

    @PutMapping("v1/countries/{countryId}/cities/{cityId}")
    public City updateCity(@PathVariable(value = "countryId") Long countryId, @PathVariable(value = "cityId") Long cityId, @Valid @RequestBody CityDto newCityDto) {
        City newCity = cityMapper.toEntity(newCityDto);
        return cityService.updateCity(countryId, cityId, newCity);
    }

    @DeleteMapping("v1/countries/{countryId}/cities/{cityId}")
    public ResponseEntity<Object> deleteCity(@PathVariable(value = "countryId") Long countryId, @PathVariable(value = "cityId") Long cityId) {
        return cityService.deleteCity(countryId, cityId);
    }
}
