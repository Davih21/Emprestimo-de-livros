package com.exampledv.livraria.database;

import android.database.sqlite.SQLiteDatabase;

public class LivroDb {

    public void inserirLivrosIniciais(SQLiteDatabase db) {
        String[] livros = {
                "('Dom Casmurro', 'Machado de Assis', 1, 'https://m.media-amazon.com/images/I/61Z2bMhGicL._SL1360_.jpg')",
                "('O Primo Basílio', 'José de Alencar', 1, 'https://m.media-amazon.com/images/I/61f2Glz3C6L._SL1360_.jpg')",
                "('As Crônicas de Nárnia - Coleção de Luxo: O Leão, a Feiticeira e o Guarda-roupa', ' C S. Lewis', 1, 'https://m.media-amazon.com/images/I/81CeSNt-57L._SL1500_.jpg')",
                "('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 1, 'https://m.media-amazon.com/images/I/71HX0h-IFpL._SL1260_.jpg')"
        };

        for (String livro : livros) {
            db.execSQL("INSERT INTO livros (titulo, autor, disponivel, imagemUrl) VALUES " + livro);
        }
    }
}
