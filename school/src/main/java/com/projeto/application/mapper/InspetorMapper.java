package com.projeto.application.mapper;

import com.projeto.application.dto.InspetorResponseDTO;
import com.projeto.domain.model.Inspetor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InspetorMapper {

    InspetorMapper INSTANCE = Mappers.getMapper(InspetorMapper.class);

    @Mapping(source = "usuario.login", target = "login")
    InspetorResponseDTO toResponseDTO(Inspetor inspetor);
}