package com.exampledv.livraria.ui.adm.livros;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.exampledv.livraria.PegarLivroActivity;
import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;
import com.exampledv.livraria.ui.adm.livros.adapter.LivroAdapter;
import com.exampledv.livraria.ui.adm.livros.models.Livro;

import java.util.ArrayList;

public class GestaoLivrosActivity extends AppCompatActivity implements LivroAdapter.OnLivroClickListener {

    private RecyclerView recyclerViewLivros;
    private Button btnAdicionarLivro;
    private DatabaseManager dbManager;
    private static final String TAG = "GestaoLivrosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestao_livros);

        recyclerViewLivros = findViewById(R.id.recyclerViewLivros);
        btnAdicionarLivro = findViewById(R.id.btnAdicionarLivro);
        dbManager = new DatabaseManager(this);

        recyclerViewLivros.setLayoutManager(new LinearLayoutManager(this));

        carregarLivros();

        btnAdicionarLivro.setOnClickListener(v -> {
            Intent intent = new Intent(GestaoLivrosActivity.this, AdicionarLivroActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLivros();
    }

    @SuppressLint("Range")
    private void carregarLivros() {
        Cursor cursor = dbManager.buscarLivros();

        if (cursor != null) {
            ArrayList<Livro> livrosList = new ArrayList<>();

            // Verificar o nome das colunas no Cursor
            String[] colunas = cursor.getColumnNames();
            for (String coluna : colunas) {
                Log.d(TAG, "Coluna encontrada: " + coluna);
            }

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                    String autor = cursor.getString(cursor.getColumnIndex("autor"));
                    int disponivel = cursor.getInt(cursor.getColumnIndex("disponivel"));

                    // Verifique se a coluna imagem_url existe antes de tentar acessá-la
                    String imagemUrl = "";
                    if (cursor.getColumnIndex("imagemUrl") != -1) {
                        imagemUrl = cursor.getString(cursor.getColumnIndex("imagemUrl"));
                    }

                    livrosList.add(new Livro(id, titulo, autor, disponivel == 1, imagemUrl));
                } while (cursor.moveToNext());
            }

            LivroAdapter adapter = new LivroAdapter(this, livrosList, this);
            recyclerViewLivros.setAdapter(adapter);

            cursor.close();
        } else {
            Log.d(TAG, "Nenhum livro encontrado.");
        }
    }


    @Override
    public void onEditClick(Livro livro) {
        Intent intent = new Intent(this, EditarLivroActivity.class);
        intent.putExtra("id", livro.getId());
        intent.putExtra("titulo", livro.getTitulo());
        intent.putExtra("autor", livro.getAutor());
        intent.putExtra("disponivel", livro.isDisponivel());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Livro livro) {
        boolean deleted = dbManager.deletarLivro(livro.getId());
        if (deleted) {
            Toast.makeText(this, "Livro deletado com sucesso", Toast.LENGTH_SHORT).show();
            carregarLivros();
        } else {
            Toast.makeText(this, "Erro ao deletar o livro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Livro livro) {
        Intent intent = new Intent(this, PegarLivroActivity.class);
        intent.putExtra("idLivro", livro.getId());
        Log.d("GestaoLivrosActivity", "Livro clicado: ID=" + livro.getId());
        intent.putExtra("titulo", livro.getTitulo());
        intent.putExtra("imgLivro", livro.getImagemUrl());
        intent.putExtra("disponivel", livro.isDisponivel());
        startActivity(intent);
    }
}