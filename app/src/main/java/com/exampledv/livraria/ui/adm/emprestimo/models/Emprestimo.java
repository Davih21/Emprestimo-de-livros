package com.exampledv.livraria.ui.adm.emprestimo.models;

public class Emprestimo {
    private int id;
    private int idUsuario;
    private int idLivro;
    private String dataEmprestimo;
    private String dataDevolucao;

    public Emprestimo(int id, int idUsuario, int idLivro, String dataEmprestimo, String dataDevolucao) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLivro = idLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }
}
