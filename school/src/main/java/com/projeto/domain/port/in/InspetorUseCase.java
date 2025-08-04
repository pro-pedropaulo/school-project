package com.projeto.domain.port.in;

import com.projeto.domain.model.Professor;

public interface InspetorUseCase {
    Professor cadastrarProfessor(String nome, String login, String senha);
    void desativarProfessor(Long id);
}