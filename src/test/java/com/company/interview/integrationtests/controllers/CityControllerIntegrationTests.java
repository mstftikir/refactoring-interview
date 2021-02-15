package com.company.interview.integrationtests.controllers;

import com.company.interview.dtos.CityDto;
import com.company.interview.entities.City;
import com.company.interview.entities.Country;
import com.company.interview.services.CityService;
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
class CityControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    private List<City> cityList;

    @BeforeEach
    void setup() {
        cityList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            City newCity = new City();
            newCity.setId((long) i);
            newCity.setName("testCity" + i);
            newCity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            newCity.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            cityList.add(newCity);
        }
    }

    @Test
    void getCitiesShouldReturnAnExpectedCity() throws Exception {
        int pageSize = 5;
        Pageable pageRequest = PageRequest.of(0, pageSize);

        Page<City> cities = new PageImpl<>(cityList, pageRequest, 10L);

        when(cityService.getCities(Mockito.any(Pageable.class))).thenReturn(cities);

        mockMvc.perform(get("/v1/cities")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("testCity5")));
    }

    @Test
    void saveCityShouldReturnOk() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("testCountry");
        country.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        country.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        City city = new City();
        city.setId(1L);
        city.setName("testCity");
        city.setCountry(country);
        city.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        city.setUpdatedTime(new Timestamp(System.currentTimeMillis()));

        CityDto cityDto = new CityDto();
        cityDto.setName("testCityDto");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(cityDto);

        when(cityService.saveCity(Mockito.any(Long.class), Mockito.any(City.class))).thenReturn(city);

        mockMvc.perform(post("/v1/countries/1/cities").contentType(APPLICATION_JSON)
                .content(requestJson)).andExpect(status().isOk());
    }

}
