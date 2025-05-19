package com.exampledv.livraria.database;

import android.database.sqlite.SQLiteDatabase;

public class UsuarioDb {

    public void inserirUsuariosIniciais(SQLiteDatabase db) {
        String[] usuarios = {
                "('Admin', 'Principal','a@gmail.com', '123456', 'admin')",
                "('Davi Oliveira', 'Principal', 'd@gmail.com', '123456', 'cliente')",
                "('Maria Santos', 'Principal', 'maria@gmail.com', 'senha456', 'cliente')",
                "('Carlos Souza' , 'Principal' , 'carlos@gmail.com', 'senha789', 'cliente')",
                "('Nicoly Miranda' , 'Integracao' , 'n@gmail.com', '123456', 'cliente')",
                "('Thiago Silva' , 'Integracao' , 't@gmail.com', '123456', 'cliente')"
        };

        for (String usuario : usuarios) {
            db.execSQL("INSERT INTO usuarios (nome, setor, email, senha, tipo) VALUES " + usuario);
        }
    }
}
