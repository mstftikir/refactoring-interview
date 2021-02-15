package com.company.interview.services;

import com.company.interview.entities.Office;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.CityRepository;
import com.company.interview.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static com.company.interview.commons.CommonMessages.*;

@Service
public class OfficeService {

    public final OfficeRepository officeRepository;

    public final CityRepository cityRepository;

    @Autowired
    public OfficeService(OfficeRepository officeRepository, CityRepository cityRepository) {
        this.officeRepository = officeRepository;
        this.cityRepository = cityRepository;
    }

    public Page<Office> getOffices(Pageable pageable) {
        return officeRepository.findAll(pageable);
    }

    public Office getOffice(@PathVariable(value = "officeId") Long officeId) {
        return officeRepository.findById(officeId).orElseThrow(() -> new ResourceNotFoundException(OFFICE_NOT_FOUND.replace("{0}", officeId.toString())));
    }

    public Page<Office> getOfficesByCityId(@PathVariable(value = "cityId") Long cityId, Pageable pageable) {
        return officeRepository.findByCityId(cityId, pageable);
    }

    public Office getOfficeByCityId(@PathVariable(value = "cityId") Long cityId, @PathVariable(value = "officeId") Long officeId) {
        return officeRepository.findByIdAndCityId(officeId, cityId).orElseThrow(() -> new ResourceNotFoundException(OFFICE_AND_CITY_NOT_FOUND.replace("{0}", officeId.toString()).replace("{1}", cityId.toString())));
    }

    public Office saveOffice(@PathVariable(value = "cityId") Long cityId, @Valid @RequestBody Office newOffice) {
        return cityRepository.findById(cityId).map(city -> {
            newOffice.setCity(city);
            return officeRepository.save(newOffice);
        }).orElseThrow(() -> new ResourceNotFoundException(CITY_NOT_FOUND.replace("{0}", cityId.toString())));
    }

    public Office updateOffice(@PathVariable(value = "cityId") Long cityId, @PathVariable(value = "officeId") Long officeId, @Valid @RequestBody Office newOffice) {
        if (!cityRepository.existsById(cityId)) {
            throw new ResourceNotFoundException(CITY_NOT_FOUND.replace("{0}", cityId.toString()));
        }

        return officeRepository.findById(officeId).map(office -> {
            office.setName(newOffice.getName());
            return officeRepository.save(office);
        }).orElseThrow(() -> new ResourceNotFoundException(OFFICE_NOT_FOUND.replace("{0}", officeId.toString())));
    }

    public ResponseEntity<Object> deleteOffice(@PathVariable(value = "cityId") Long cityId, @PathVariable(value = "officeId") Long officeId) {
        return officeRepository.findByIdAndCityId(officeId, cityId).map(office -> {
            officeRepository.delete(office);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(OFFICE_AND_CITY_NOT_FOUND.replace("{0}", officeId.toString()).replace("{1}", cityId.toString())));
    }
}
