package com.exampledv.livraria.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome e versão do banco
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de usuários
        db.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "setor TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL, " +
                "tipo TEXT NOT NULL" + // "admin" ou "cliente"
                ");");

        // Criação da tabela de livros
        db.execSQL("CREATE TABLE livros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "titulo TEXT NOT NULL, " +
                "autor TEXT NOT NULL, " +
                "disponivel INTEGER DEFAULT 1, " +  // 1 = Disponível, 0 = Emprestado
                "imagemUrl TEXT, " + // Coluna para armazenar a URL da imagem
                "nomePessoa TEXT" +
                ");");
        // Criação da tabela de empréstimos
        db.execSQL("CREATE TABLE emprestimos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idUsuario INTEGER NOT NULL, " +
                "idLivro INTEGER NOT NULL, " +
                "dataEmprestimo TEXT NOT NULL, " +
                "dataDevolucao TEXT, " +
                "FOREIGN KEY(idUsuario) REFERENCES usuarios(id), " +
                "FOREIGN KEY(idLivro) REFERENCES livros(id)" +
                ");");

        // Inicializa a classe UsuarioDAO e insere os usuários
        UsuarioDb usuarioDb = new UsuarioDb();
        usuarioDb.inserirUsuariosIniciais(db);

        // Inserir livro padrão
        LivroDb livroDb = new LivroDb();
        livroDb.inserirLivrosIniciais(db);

        // Adicionar índices
        db.execSQL("CREATE INDEX idx_livros_disponivel ON livros(disponivel);");
        db.execSQL("CREATE INDEX idx_emprestimos_usuario ON emprestimos(idUsuario);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualização: Remover tabelas antigas e recriá-las
        db.execSQL("DROP TABLE IF EXISTS emprestimos");
        db.execSQL("DROP TABLE IF EXISTS livros");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Ativar suporte a chaves estrangeiras
        db.setForeignKeyConstraintsEnabled(true);
    }
}