package com.projeto.application.mapper;

import com.projeto.application.dto.ProfessorResponseDTO;
import com.projeto.domain.model.Professor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    ProfessorMapper INSTANCE = Mappers.getMapper(ProfessorMapper.class);

    @Mapping(source = "usuario.login", target = "login")
    ProfessorResponseDTO toResponseDTO(Professor professor);
}