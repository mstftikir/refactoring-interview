package com.company.interview.services;

import com.company.interview.entities.City;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.CityRepository;
import com.company.interview.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.company.interview.commons.CommonMessages.*;

@Service
public class CityService {

    public final CityRepository cityRepository;

    public final CountryRepository countryRepository;

    @Autowired
    public CityService(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    public Page<City> getCities(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    public City getCity(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(() -> new ResourceNotFoundException(CITY_NOT_FOUND.replace("{0}", cityId.toString())));
    }

    public Page<City> getCitiesByCountryId(Long countryId, Pageable pageable) {
        return cityRepository.findByCountryId(countryId, pageable);
    }

    public City getCityByCountryId(Long countryId, Long cityId) {
        return cityRepository.findByIdAndCountryId(cityId, countryId).orElseThrow(() -> new ResourceNotFoundException(CITY_AND_COUNTRY_NOT_FOUND.replace("{0}", cityId.toString()).replace("{1}", countryId.toString())));
    }

    public City saveCity(Long countryId, City newCity) {
        return countryRepository.findById(countryId).map(country -> {
            newCity.setCountry(country);
            return cityRepository.save(newCity);
        }).orElseThrow(() -> new ResourceNotFoundException(COUNTRY_NOT_FOUND.replace("{0}", countryId.toString())));
    }

    public City updateCity(Long countryId, Long cityId, City newCity) {
        if (!countryRepository.existsById(countryId)) {
            throw new ResourceNotFoundException(COUNTRY_NOT_FOUND.replace("{0}", countryId.toString()));
        }

        return cityRepository.findById(cityId).map(city -> {
            city.setName(newCity.getName());
            return cityRepository.save(city);
        }).orElseThrow(() -> new ResourceNotFoundException(CITY_NOT_FOUND.replace("{0}", cityId.toString())));
    }

    public ResponseEntity<Object> deleteCity(Long countryId, Long cityId) {
        return cityRepository.findByIdAndCountryId(cityId, countryId).map(city -> {
            cityRepository.delete(city);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(CITY_AND_COUNTRY_NOT_FOUND.replace("{0}", cityId.toString()).replace("{1}", countryId.toString())));
    }

}
