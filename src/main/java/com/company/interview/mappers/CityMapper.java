package com.company.interview.mappers;

import com.company.interview.dtos.CityDto;
import com.company.interview.entities.City;
import org.mapstruct.Mapper;

@Mapper
public interface CityMapper {
    City toEntity(CityDto cityDto);
}
