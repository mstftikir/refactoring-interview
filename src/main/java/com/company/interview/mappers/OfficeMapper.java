package com.company.interview.mappers;

import com.company.interview.dtos.OfficeDto;
import com.company.interview.entities.Office;
import org.mapstruct.Mapper;

@Mapper
public interface OfficeMapper {
    Office toEntity(OfficeDto officeDto);
}
