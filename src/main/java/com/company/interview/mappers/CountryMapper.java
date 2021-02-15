package com.company.interview.mappers;

import com.company.interview.dtos.CountryDto;
import com.company.interview.entities.Country;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {
    Country toEntity(CountryDto countryDto);
}
