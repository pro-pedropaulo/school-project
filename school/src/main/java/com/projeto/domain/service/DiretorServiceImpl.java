package com.projeto.domain.service;

import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import com.projeto.domain.model.Inspetor;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.in.DiretorUseCase;
import com.projeto.domain.port.out.InspetorPersistencePort;
import com.projeto.domain.port.out.UsuarioPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DiretorServiceImpl implements DiretorUseCase {

    private final InspetorPersistencePort inspetorPersistencePort;
    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public DiretorServiceImpl(InspetorPersistencePort inspetorPersistencePort, UsuarioPersistencePort usuarioPersistencePort, PasswordEncoder passwordEncoder) {
        this.inspetorPersistencePort = inspetorPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Inspetor cadastrarInspetor(String nome, String login, String senha) {
        log.info("Iniciando cadastro de inspetor com login: {}", login);

        usuarioPersistencePort.buscarPorLogin(login).ifPresent(u -> {
            throw new BusinessException("Login já está em uso.");
        });

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(login);
        novoUsuario.setSenha(passwordEncoder.encode(senha));
        novoUsuario.setRole(Role.INSPETOR);
        novoUsuario.setAtivo(true);

        Inspetor novoInspetor = new Inspetor();
        novoInspetor.setNome(nome);
        novoInspetor.setUsuario(novoUsuario);

        Inspetor inspetorSalvo = inspetorPersistencePort.salvar(novoInspetor);
        log.info("Inspetor {} cadastrado com sucesso com o id: {}", inspetorSalvo.getNome(), inspetorSalvo.getId());
        return inspetorSalvo;
    }

    @Transactional
    @Override
    public void desativarInspetor(Long id) {
        log.info("Iniciando desativação do inspetor com id: {}", id);
        Inspetor inspetor = inspetorPersistencePort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inspetor", "id", id));

        Usuario usuarioParaDesativar = inspetor.getUsuario();
        usuarioParaDesativar.setAtivo(false);
        usuarioPersistencePort.salvar(usuarioParaDesativar);
        log.info("Inspetor com id {} desativado com sucesso.", id);
    }
}