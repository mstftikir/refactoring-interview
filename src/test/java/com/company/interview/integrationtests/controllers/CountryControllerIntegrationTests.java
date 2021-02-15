package com.company.interview.integrationtests.controllers;

import com.company.interview.dtos.CountryDto;
import com.company.interview.entities.Country;
import com.company.interview.services.CountryService;
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
class CountryControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    private List<Country> countryList;

    @BeforeEach
    void setup() {
        countryList = new LinkedList<>();
        for (int i = 0; i < 10 ; i++) {
            Country newCountry = new Country();
            newCountry.setId((long) i);
            newCountry.setName("testCountry" + i);
            newCountry.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newCountry.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            countryList.add(newCountry);
        }
    }

    @Test
    void getCountriesShouldReturnAnExpectedCountry() throws Exception {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<Country> countries = new PageImpl<>(countryList, pageRequest, 10L);

        when(countryService.getCountries(Mockito.any(Pageable.class))).thenReturn(countries);

        mockMvc.perform(get("/v1/countries")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("testCountry5")));
    }

    @Test
    void saveCountryShouldReturnOk() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("testCountry");
        country.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        country.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        CountryDto countryDto = new CountryDto();
        countryDto.setName("testCountryDto");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(countryDto);

        when(countryService.saveCountry(Mockito.any(Country.class))).thenReturn(country);

        mockMvc.perform(post("/v1/countries").contentType(APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isOk());
    }
}
