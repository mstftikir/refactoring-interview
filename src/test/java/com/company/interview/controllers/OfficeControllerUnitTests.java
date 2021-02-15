package com.company.interview.controllers;

import com.company.interview.entities.City;
import com.company.interview.entities.Office;
import com.company.interview.exceptions.ResourceNotFoundException;
import com.company.interview.repositories.CityRepository;
import com.company.interview.repositories.OfficeRepository;
import com.company.interview.services.OfficeService;
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

public class OfficeControllerUnitTests {

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private OfficeService officeService;

    private Office office;

    private List<Office> officeList;

    private City city;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        office = new Office();
        office.setId(1L);
        office.setName("testOffice");
        office.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        office.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        city = new City();
        city.setId(1L);
        city.setName("testCity");
        city.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        city.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        officeList = new LinkedList<>();

        for (int i = 0; i < 10 ; i++) {
            Office newOffice = new Office();
            newOffice.setId((long) i);
            newOffice.setName("test" + i);
            newOffice.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newOffice.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            newOffice.setCity(city);
            officeList.add(newOffice);
        }
    }

    @Test
    public void getOfficesTest() {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<Office> offices = new PageImpl<>(officeList, pageRequest, 10L);

        when(officeRepository.findAll(Mockito.any(Pageable.class))).thenReturn(offices);

        Page<Office> officesReturned = officeService.getOffices(pageRequest);

        assertNotNull(officesReturned);
        assertEquals(officeList.size() / pageSize, officesReturned.getTotalPages());
        assertEquals(officeList.get(0), officesReturned.getContent().get(0));
    }

    @Test
    public void getOfficeTest() {
        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        Office officeReturned = officeService.getOffice(1L);

        assertNotNull(officeReturned);
        assertEquals(office, officeReturned);
    }

    @Test
    public void getOfficeThrowsExceptionTest() {
        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        assertThrows(ResourceNotFoundException.class, () -> officeService.getOffice(2L));
    }

    @Test
    public void getOfficesByCityIdTest(){
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);
        Page<Office> offices = new PageImpl<>(officeList, pageRequest, 10L);

        when(officeRepository.findByCityId(1L, pageRequest)).thenReturn(offices);

        Page<Office> officesReturned = officeService.getOfficesByCityId(1L, pageRequest);

        assertNotNull(officesReturned);
        assertEquals(officeList.size() / pageSize, officesReturned.getTotalPages());
        assertEquals(officeList.get(0), officesReturned.getContent().get(0));
    }

    @Test
    public void getOfficeByCityIdTest(){
        when(officeRepository.findByIdAndCityId(10L, 1L)).thenReturn(Optional.of(office));

        Office officeReturned = officeService.getOfficeByCityId(1L, 10L);

        assertNotNull(officeReturned);
        assertEquals(office, officeReturned);
    }

    @Test
    public void getOfficeByCityIdThrowsExceptionTest(){
        when(officeRepository.findByIdAndCityId(10L, 1L)).thenReturn(Optional.of(office));

        assertThrows(ResourceNotFoundException.class, () -> officeService.getOfficeByCityId(10L, 2L));
    }

    @Test
    public void saveOfficeTest(){
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(officeRepository.save(Mockito.any(Office.class))).thenReturn(office);

        Office officeReturned = officeService.saveOffice(1L, office);

        assertNotNull(officeReturned);
        assertEquals(office, officeReturned);
    }

    @Test
    public void saveOfficeThrowsExceptionTest(){
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        assertThrows(ResourceNotFoundException.class, () -> officeService.saveOffice(2L, office));
    }

    @Test
    public void updateOfficeTest(){
        when(cityRepository.existsById(1L)).thenReturn(true);
        when(officeRepository.save(office)).thenReturn(office);
        when(officeRepository.findById(10L)).thenReturn(Optional.of(office));

        Office officeReturned = officeService.updateOffice(1L, 10L, office);

        assertNotNull(officeReturned);
        assertEquals(office, officeReturned);
    }

    @Test
    public void updateOfficeNotFoundExceptionTest(){
        when(cityRepository.existsById(1L)).thenReturn(true);
        when(officeRepository.save(office)).thenReturn(office);
        when(officeRepository.findById(10L)).thenReturn(Optional.of(office));

        assertThrows(ResourceNotFoundException.class, () -> officeService.updateOffice(1L, 2L, office));
    }

    @Test
    public void updateOfficeCityNotFoundExceptionTest(){
        when(officeRepository.save(office)).thenReturn(office);
        when(officeRepository.findById(10L)).thenReturn(Optional.of(office));

        assertThrows(ResourceNotFoundException.class, () -> officeService.updateOffice(1L, 2L, office));
    }

    @Test
    public void deleteOfficeTest(){
        when(officeRepository.findByIdAndCityId(10L, 1L)).thenReturn(Optional.of(office));

        doNothing().when(officeRepository).delete(ArgumentMatchers.any(Office.class));

        ResponseEntity<?> responseEntity = officeService.deleteOffice(1L, 10L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteOfficeNotFoundExceptionTest(){
        when(officeRepository.findByIdAndCityId(10L, 1L)).thenReturn(Optional.of(office));

        assertThrows(ResourceNotFoundException.class, () -> officeService.deleteOffice(1L, 2L));
    }
}
