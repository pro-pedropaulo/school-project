package com.projeto.domain.service;

import com.projeto.application.exception.BusinessException;
import com.projeto.application.exception.ResourceNotFoundException;
import com.projeto.domain.model.Professor;
import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.domain.port.in.InspetorUseCase;
import com.projeto.domain.port.out.UsuarioPersistencePort;
import com.projeto.school.domain.port.out.ProfessorPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class InspetorServiceImpl implements InspetorUseCase {

    private final ProfessorPersistencePort professorPersistencePort;
    private final UsuarioPersistencePort usuarioPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public InspetorServiceImpl(ProfessorPersistencePort professorPersistencePort, UsuarioPersistencePort usuarioPersistencePort, PasswordEncoder passwordEncoder) {
        this.professorPersistencePort = professorPersistencePort;
        this.usuarioPersistencePort = usuarioPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Professor cadastrarProfessor(String nome, String login, String senha) {
        log.info("Iniciando cadastro de professor com login: {}", login);
        usuarioPersistencePort.buscarPorLogin(login).ifPresent(u -> {
            throw new BusinessException("Login já está em uso.");
        });

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(login);
        novoUsuario.setSenha(passwordEncoder.encode(senha));
        novoUsuario.setRole(Role.PROFESSOR);
        novoUsuario.setAtivo(true);

        Professor novoProfessor = new Professor();
        novoProfessor.setNome(nome);
        novoProfessor.setUsuario(novoUsuario);

        Professor professorSalvo = professorPersistencePort.salvar(novoProfessor);
        log.info("Professor {} cadastrado com sucesso com o id: {}", professorSalvo.getNome(), professorSalvo.getId());
        return professorSalvo;
    }

    @Transactional
    @Override
    public void desativarProfessor(Long id) {
        log.info("Iniciando desativação do professor com id: {}", id);
        Professor professor = professorPersistencePort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor", "id", id));

        Usuario usuarioParaDesativar = professor.getUsuario();
        usuarioParaDesativar.setAtivo(false);
        usuarioPersistencePort.salvar(usuarioParaDesativar);
        log.info("Professor com id {} desativado com sucesso.", id);
    }
}