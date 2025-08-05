package com.projeto.domain.port.in;

import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;

import java.time.LocalDate;
import java.util.List;

public interface PresencaUseCase {
    Presenca registrarPresenca(String loginUsuario, LocalDate data);
    List<Presenca> buscarPresencasPorUsuario(String loginUsuario);
    List<Presenca> buscarPresencasPorDataERole(LocalDate data, Role role);
}