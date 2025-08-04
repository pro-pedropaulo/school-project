package com.projeto;

import com.projeto.domain.model.Role;
import com.projeto.domain.model.Usuario;
import com.projeto.infrastructure.repository.jpa.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.buscarPorLogin("diretor").isEmpty()) {
            Usuario diretor = new Usuario();
            diretor.setLogin("diretor");
            diretor.setSenha(passwordEncoder.encode("password"));
            diretor.setRole(Role.DIRETOR);
            diretor.setAtivo(true);
            usuarioRepository.save(diretor);
        }
    }
}