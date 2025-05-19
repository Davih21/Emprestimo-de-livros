package com.exampledv.livraria.ui.adm.livros.models;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private boolean disponivel;
    private String imagemUrl;

    // Construtor atualizado
    public Livro(int id, String titulo, String autor, boolean disponivel, String imagemUrl) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = disponivel;
        this.imagemUrl = imagemUrl;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
