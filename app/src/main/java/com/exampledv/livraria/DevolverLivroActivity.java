package com.exampledv.livraria;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exampledv.livraria.database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class DevolverLivroActivity extends FullScreenActivity {

    private ImageView pg_fotoLivro;
    private TextView pg_nomeLivro;
    private TextView pgNomePessoa;
    private TextView pgSetor;
    private Button btnFinalizarDevolucao, btnFinalizarDevolucaoPrinter;

    private DatabaseManager dbManager;
    private List<Integer> livrosEmprestadosIds;
    private int currentPosition = 0;
    private int idLivroAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolver_livro);

        pg_fotoLivro = findViewById(R.id.pg_fotoLivro);
        pg_nomeLivro = findViewById(R.id.pg_nomeLivro);
        pgNomePessoa = findViewById(R.id.pgNomePessoa);
        pgSetor = findViewById(R.id.pgSetor);
        btnFinalizarDevolucao = findViewById(R.id.btnFinalizarDevolucao);
        btnFinalizarDevolucaoPrinter = findViewById(R.id.btnFinalizarDevolucaoPrinter);

        dbManager = new DatabaseManager(this);

        String nome = getIntent().getStringExtra("nome");
        String setor = getIntent().getStringExtra("setor");

        pgNomePessoa.setText(nome);
        pgSetor.setText(setor);


        livrosEmprestadosIds = obterLivrosEmprestados();

        if (livrosEmprestadosIds.isEmpty()) {
            Toast.makeText(this, "Nenhum livro emprestado encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrarLivroAtual();

        btnFinalizarDevolucao.setOnClickListener(v -> devolverLivro());
        btnFinalizarDevolucaoPrinter.setOnClickListener(v -> devolverLivroNaTela());
    }

    private List<Integer> obterLivrosEmprestados() {
        List<Integer> ids = new ArrayList<>();
        String nomeUsuarioAtual = getIntent().getStringExtra("nome");
        Cursor cursor = dbManager.buscarLivrosEmprestadosPorUsuario(nomeUsuarioAtual);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return ids;
    }

    private void mostrarLivroAtual() {
        if (currentPosition >= 0 && currentPosition < livrosEmprestadosIds.size()) {
            idLivroAtual = livrosEmprestadosIds.get(currentPosition);
            carregarDadosLivro(idLivroAtual);
//            atualizarBotoesNavegacao();
        }
    }


    private void carregarDadosLivro(int livroId) {
        Cursor cursor = dbManager.buscarLivroPorId(livroId);

        if (cursor != null && cursor.moveToFirst()) {
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
            String imgUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagemUrl"));

            pg_nomeLivro.setText(titulo);
            Glide.with(this).load(imgUrl).into(pg_fotoLivro);

            cursor.close();
        }
    }

    private void devolverLivro() {
        int idUsuario = getIntent().getIntExtra("idUsuario", -1);

        boolean sucesso = dbManager.devolverLivro(idLivroAtual, idUsuario);

        String nomeUsuarioAtual = getIntent().getStringExtra("nome");
//        Cursor cursor = dbManager.buscarLivrosEmprestadosPorUsuario(nomeUsuarioAtual);

        if (sucesso) {
            Toast.makeText(this, "Livro devolvido com sucesso!", Toast.LENGTH_SHORT).show();
            livrosEmprestadosIds.remove(currentPosition);

            // Abrir a tela CupomNaTela passando o id do livro e do usuário, se necessário
            Intent intent = new Intent(this, CupomNaTelaDevolucao.class);
            intent.putExtra("nome", nomeUsuarioAtual);
            intent.putExtra("setor", getIntent().getStringExtra("setor"));
            startActivity(intent);
            finish(); // Finaliza a activity atual se você não quiser voltar nela

//            if (livrosEmprestadosIds.isEmpty()) {
//                setResult(RESULT_OK);
//                finish();
//            } else {
//                if (currentPosition >= livrosEmprestadosIds.size()) {
//                    currentPosition = livrosEmprestadosIds.size() - 1;
//                }
//                mostrarLivroAtual();
//            }
        } else {
            Toast.makeText(this, "Erro: livro não está emprestado para este usuário", Toast.LENGTH_SHORT).show();
        }
    }


    private void devolverLivroNaTela() {
        int idUsuario = getIntent().getIntExtra("idUsuario", -1);

        boolean sucesso = dbManager.devolverLivro(idLivroAtual, idUsuario);

        String nomeUsuarioAtual = getIntent().getStringExtra("nome");
//        Cursor cursor = dbManager.buscarLivrosEmprestadosPorUsuario(nomeUsuarioAtual);

        if (sucesso) {
            Toast.makeText(this, "Livro devolvido com sucesso!", Toast.LENGTH_SHORT).show();
            livrosEmprestadosIds.remove(currentPosition);

            // Abrir a tela CupomNaTela passando o id do livro e do usuário, se necessário
            Intent intent = new Intent(this, PrinterDevolucao.class);
            intent.putExtra("nome", nomeUsuarioAtual);
            intent.putExtra("setor", getIntent().getStringExtra("setor"));
            startActivity(intent);
            finish(); // Finaliza a activity atual se você não quiser voltar nela

        } else {
            Toast.makeText(this, "Erro: livro não está emprestado para este usuário", Toast.LENGTH_SHORT).show();
        }
    }

}