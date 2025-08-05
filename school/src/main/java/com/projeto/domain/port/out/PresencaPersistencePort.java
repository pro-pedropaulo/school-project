package com.projeto.domain.port.out;

import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface PresencaPersistencePort {
    boolean existePresencaPorUsuarioEData(Usuario usuario, LocalDate data);
    Presenca salvar(Presenca presenca);
    List<Presenca> buscarPorUsuario(Usuario usuario);
    List<Presenca> buscarPorDataERole(LocalDate data, Role role);
}