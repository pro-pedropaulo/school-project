package com.projeto.infrastructure.repository.adapter;

import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.out.PresencaPersistencePort;
import com.projeto.infrastructure.repository.jpa.PresencaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PresencaPersistenceAdapter implements PresencaPersistencePort {

    private final PresencaRepository presencaRepository;

    public PresencaPersistenceAdapter(PresencaRepository presencaRepository) {
        this.presencaRepository = presencaRepository;
    }

    @Override
    public boolean existePresencaPorUsuarioEData(Usuario usuario, LocalDate data) {
        return presencaRepository.existsByUsuarioAndData(usuario, data);
    }

    @Override
    public Presenca salvar(Presenca presenca) {
        return presencaRepository.save(presenca);
    }

    @Override
    public List<Presenca> buscarPorUsuario(Usuario usuario) {
        return presencaRepository.findByUsuario(usuario);
    }

    @Override
    public List<Presenca> buscarPorDataERole(LocalDate data, Role role) {
        return presencaRepository.findByDataAndUsuarioRole(data, role);
    }
}