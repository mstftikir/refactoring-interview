package com.company.interview.controllers;

import com.company.interview.dtos.OfficeDto;
import com.company.interview.entities.Office;
import com.company.interview.mappers.OfficeMapper;
import com.company.interview.services.OfficeService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class OfficeController {

    private final OfficeMapper officeMapper = Mappers.getMapper(OfficeMapper.class);

    public final OfficeService officeService;

    @Autowired
    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("v1/offices")
    public Page<Office> getOffices(Pageable pageable) {
        return officeService.getOffices(pageable);
    }

    @GetMapping("v1/offices/{officeId}")
    public Office getOffice(@PathVariable(value = "officeId") Long officeId) {
        return officeService.getOffice(officeId);
    }

    @GetMapping("v1/cities/{cityId}/offices")
    public Page<Office> getOfficesByCityId(@PathVariable(value = "cityId") Long cityId, Pageable pageable) {
        return officeService.getOfficesByCityId(cityId, pageable);
    }

    @GetMapping("v1/cities/{cityId}/offices/{officeId}")
    public Office getOfficeByCityId(@PathVariable(value = "cityId") Long cityId, @PathVariable(value = "officeId") Long officeId) {
        return officeService.getOfficeByCityId(cityId, officeId);
    }

    @PostMapping("v1/cities/{cityId}/offices")
    public Office saveOffice(@PathVariable(value = "cityId") Long cityId, @Valid @RequestBody OfficeDto newOfficeDto) {
        Office newOffice = officeMapper.toEntity(newOfficeDto);
        return officeService.saveOffice(cityId, newOffice);
    }


    @PutMapping("v1/cities/{cityId}/offices/{officeId}")
    public Office updateOffice(@PathVariable(value = "cityId") Long cityId, @PathVariable(value = "officeId") Long officeId, @Valid @RequestBody OfficeDto newOfficeDto) {
        Office newOffice = officeMapper.toEntity(newOfficeDto);
        return officeService.updateOffice(cityId, officeId, newOffice);
    }

    @DeleteMapping("v1/cities/{cityId}/offices/{officeId}")
    public ResponseEntity<Object> deleteOffice(@PathVariable(value = "cityId") Long cityId, @PathVariable(value = "officeId") Long officeId) {
        return officeService.deleteOffice(cityId, officeId);
    }
}
