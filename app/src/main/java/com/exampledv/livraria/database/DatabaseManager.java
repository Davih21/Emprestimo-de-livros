package com.exampledv.livraria.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseManager {
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    //Usuario
    public long adicionarUsuario(String nome, String setor, String email, String senha, String tipo) {
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("setor", setor);
        valores.put("email", email);
        valores.put("senha", senha);
        valores.put("tipo", tipo);
        return db.insert("usuarios", null, valores);
    }

    public boolean atualizarUsuario(int id, String nome, String setor, String email, String senha, String tipo) {
        ContentValues valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("setor", setor);
        valores.put("email", email);
        valores.put("senha", senha);
        valores.put("tipo", tipo);

        int rowsAffected = db.update("usuarios", valores, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public boolean deletarUsuario(int id) {
        int rowsAffected = db.delete("usuarios", "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public Cursor buscarUsuarios() {
        return db.rawQuery("SELECT * FROM usuarios", null);
    }

    public Cursor buscarUsuarioPorEmail(String email) {
        return db.rawQuery("SELECT * FROM usuarios WHERE email = ?", new String[]{email});
    }

    //Livro
    public long adicionarLivro(String titulo, String autor, boolean disponivel, String imagemUrl) {
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("autor", autor);
        valores.put("disponivel", disponivel ? 1 : 0);
        valores.put("imagemUrl", imagemUrl);
        return db.insert("livros", null, valores);
    }


    public Cursor buscarLivros() {
        return db.rawQuery("SELECT * FROM livros", null);
    }

    public Cursor buscarLivroPorId(int id) {
        return db.rawQuery("SELECT * FROM livros WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public boolean atualizarLivro(int id, String titulo, String autor, boolean disponivel, String imagemUrl) {
        ContentValues valores = new ContentValues();
        valores.put("titulo", titulo);
        valores.put("autor", autor);
        valores.put("disponivel", disponivel ? 1 : 0);
        valores.put("imagemUrl", imagemUrl);

        int rowsAffected = db.update("livros", valores, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public boolean deletarLivro(int id) {
        int rowsAffected = db.delete("livros", "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }


    public Cursor buscarLivrosPorNome(String nome) {
        return db.rawQuery("SELECT * FROM livros WHERE titulo LIKE ?", new String[]{"%" + nome + "%"});
    }

    public Cursor buscarTodosLivros() {
        return db.rawQuery("SELECT * FROM livros", null);
    }

    public boolean devolverLivro(int idLivro, int idUsuario) {
        try {
            db.beginTransaction();

            // 1. Verifica se o livro está emprestado PARA ESTE USUÁRIO
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM emprestimos WHERE idLivro = ? AND idUsuario = ?",
                    new String[]{String.valueOf(idLivro), String.valueOf(idUsuario)}
            );

            if (cursor == null || !cursor.moveToFirst()) {
                // Livro não está emprestado para este usuário
                if (cursor != null) cursor.close();
                return false;
            }
            cursor.close();

            // 2. Marca livro como disponível
            ContentValues livroValues = new ContentValues();
            livroValues.put("disponivel", 1);
            db.update("livros", livroValues, "id = ?", new String[]{String.valueOf(idLivro)});

            // 3. Remove registro de empréstimo específico para este usuário-livro
            db.delete("emprestimos",
                    "idLivro = ? AND idUsuario = ?",
                    new String[]{String.valueOf(idLivro), String.valueOf(idUsuario)});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }
    //Fiz alteração aqui

    public Cursor buscarLivrosEmprestadosPorUsuario(String nomeUsuario) {


        String query = "SELECT livros.id FROM livros " +
                "INNER JOIN emprestimos ON livros.id = emprestimos.idLivro " +
                "INNER JOIN usuarios ON emprestimos.idUsuario = usuarios.id " +
                "WHERE livros.disponivel = 0 AND usuarios.nome = ?";

        return db.rawQuery(query, new String[]{nomeUsuario});
    }
    public int obterIdUsuarioPorNome(String nome) {

        Cursor cursor = db.query(
                "usuarios",
                new String[]{"id"},
                "nome = ?",
                new String[]{nome},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return id;
        }

        if (cursor != null) {
            cursor.close();
        }
        return -1;
    }

    public boolean emprestarLivro(int idLivro, int idUsuario) {
        try {
            // Inicia transação
            db.beginTransaction();

            // 1. Marca livro como indisponível
            ContentValues livroValues = new ContentValues();
            livroValues.put("disponivel", 0);
            db.update("livros", livroValues, "id = ?", new String[]{String.valueOf(idLivro)});

            // 2. Cria registro de empréstimo
            ContentValues emprestimoValues = new ContentValues();
            emprestimoValues.put("idUsuario", idUsuario);
            emprestimoValues.put("idLivro", idLivro);
            emprestimoValues.put("dataEmprestimo", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            db.insert("emprestimos", null, emprestimoValues);

            // Confirma transação
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }
}
