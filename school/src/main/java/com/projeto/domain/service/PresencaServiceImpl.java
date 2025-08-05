package com.projeto.domain.service;

import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import com.projeto.domain.model.Presenca;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.in.PresencaUseCase;
import com.projeto.domain.port.out.PresencaPersistencePort;
import com.projeto.domain.port.out.UsuarioPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class PresencaServiceImpl implements PresencaUseCase {

    private final PresencaPersistencePort presencaPersistencePort;
    private final UsuarioPersistencePort usuarioPersistencePort;

    public PresencaServiceImpl(PresencaPersistencePort presencaPersistencePort, UsuarioPersistencePort usuarioPersistencePort) {
        this.presencaPersistencePort = presencaPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
    }

    @Override
    public Presenca registrarPresenca(String loginUsuario, LocalDate data) {
        log.info("Tentativa de registro de presença para o usuário {} na data {}", loginUsuario, data);
        Usuario usuario = usuarioPersistencePort.buscarPorLogin(loginUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", loginUsuario));

        if (presencaPersistencePort.existePresencaPorUsuarioEData(usuario, data)) {
            throw new BusinessException("Presença já registrada para este usuário na data especificada.");
        }

        Presenca novaPresenca = new Presenca();
        novaPresenca.setUsuario(usuario);
        novaPresenca.setData(data);

        Presenca presencaSalva = presencaPersistencePort.salvar(novaPresenca);
        log.info("Presença registrada com sucesso com id {}", presencaSalva.getId());
        return presencaSalva;
    }

    @Override
    public List<Presenca> buscarPresencasPorUsuario(String loginUsuario) {
        log.info("Buscando histórico de presença para o usuário {}", loginUsuario);
        Usuario usuario = usuarioPersistencePort.buscarPorLogin(loginUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "login", loginUsuario));
        return presencaPersistencePort.buscarPorUsuario(usuario);
    }

    @Override
    public List<Presenca> buscarPresencasPorDataERole(LocalDate data, Role role) {
        log.info("Buscando presenças para a data {} e role {}", data, role);
        return presencaPersistencePort.buscarPorDataERole(data, role);
    }
}