package com.exampledv.livraria.ui.adm.usuarios.models;

public class Usuario {
    private int id;
    private String nome;
    private String setor;
    private String email;
    private String tipo;

    // Construtor
    public Usuario(int id, String nome, String setor,String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.setor = setor;
        this.email = email;
        this.tipo = tipo;
    }

    // Métodos getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSetor() {
        return setor;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }


    public void setId(int id) {
        this.id = id;
    }
}
