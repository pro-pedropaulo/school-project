package com.projeto.domain.port.in;

import com.projeto.domain.model.Inspetor;

import java.util.List;

public interface DiretorUseCase {
    Inspetor cadastrarInspetor(String nome, String login, String senha);
    void desativarInspetor(Long id);
    List<Inspetor> listarInspetoresAtivos();
}