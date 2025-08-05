package com.projeto.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "presencas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "data"}))
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate data;

    public Presenca() {
    }

    public Presenca(Long id, Usuario usuario, LocalDate data) {
        this.id = id;
        this.usuario = usuario;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Presenca presenca = (Presenca) o;
        return Objects.equals(id, presenca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}