package com.projeto.application.mapper;

import com.projeto.application.dto.PresencaResponseDTO;
import com.projeto.domain.model.Presenca;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PresencaMapper {

    PresencaMapper INSTANCE = Mappers.getMapper(PresencaMapper.class);

    @Mapping(source = "usuario.login", target = "loginUsuario")
    PresencaResponseDTO toResponseDTO(Presenca presenca);
}