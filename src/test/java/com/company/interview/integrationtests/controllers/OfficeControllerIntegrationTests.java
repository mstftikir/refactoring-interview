package com.company.interview.integrationtests.controllers;

import com.company.interview.dtos.OfficeDto;
import com.company.interview.entities.City;
import com.company.interview.entities.Office;
import com.company.interview.services.OfficeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
 class OfficeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfficeService officeService;

    private List<Office> officeList;

    @BeforeEach
    void setup() {
        officeList = new LinkedList<>();
        for (int i = 0; i < 10 ; i++) {
            Office newOffice = new Office();
            newOffice.setId((long) i);
            newOffice.setName("testOffice" + i);
            newOffice.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newOffice.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            officeList.add(newOffice);
        }
    }

    @Test
    void getOfficesShouldReturnAnExpectedOffice() throws Exception {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<Office> offices = new PageImpl<>(officeList, pageRequest, 10L);

        when(officeService.getOffices(Mockito.any(Pageable.class))).thenReturn(offices);

        mockMvc.perform(get("/v1/offices")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("testOffice")));
    }

    @Test
    void saveOfficeShouldReturnOk() throws Exception {
        City city = new City();
        city.setId(1L);
        city.setName("testCity");
        city.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        city.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        Office office = new Office();
        office.setId(1L);
        office.setName("testOffice");
        office.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        office.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        office.setCity(city);

        OfficeDto officeDto = new OfficeDto();
        officeDto.setName("testOfficeDto");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(officeDto);

        when(officeService.saveOffice( Mockito.any(Long.class), Mockito.any(Office.class))).thenReturn(office);

        mockMvc.perform(post("/v1/cities/1/offices").contentType(APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isOk());
    }
}
