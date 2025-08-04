package com.projeto.domain.port.in;

import com.projeto.domain.model.Inspetor;

public interface DiretorUseCase {
    Inspetor cadastrarInspetor(String nome, String login, String senha);
    void desativarInspetor(Long id);
}