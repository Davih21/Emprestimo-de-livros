package com.exampledv.livraria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.exampledv.livraria.database.DatabaseManager;

public class PegarLivroActivity extends FullScreenActivity {

    private ImageView pg_fotoLivro;
    private TextView pg_nomeLivro;
    private TextView pgNomePessoa;
    private TextView pgSetor;
    private Button btnFinalizarEmprestimo, btnComprovante;

    private boolean disponivel; // Agora é GLOBAL e funciona em qualquer método


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegar_livro);

        pg_fotoLivro = findViewById(R.id.pg_fotoLivro);
        pg_nomeLivro = findViewById(R.id.pg_nomeLivro);
        pgNomePessoa = findViewById(R.id.pgNomePessoa);
        pgSetor = findViewById(R.id.pgSetor);
        btnFinalizarEmprestimo = findViewById(R.id.btnFinalizarEmprestimo);
        btnComprovante = findViewById(R.id.btnComprovante);

        // Pegando dados enviados da tela anterior

        String nome = getIntent().getExtras().getString("nome");
        String setor = getIntent().getExtras().getString("setor");
        String imgLivro = getIntent().getExtras().getString("imgLivro");
        String titulo = getIntent().getExtras().getString("titulo");
        disponivel = getIntent().getBooleanExtra("disponivel", false);


        // Setando nas TextViews
        Glide.with(getApplicationContext()).load(imgLivro).into(pg_fotoLivro);
        pg_nomeLivro.setText(titulo);
        pgNomePessoa.setText(nome);
        pgSetor.setText(setor);


        btnFinalizarEmprestimo.setOnClickListener(v -> emprestarLivro());

        btnComprovante.setOnClickListener(v -> emprestarLivroTela());
    }

    private void emprestarLivro() {
        int idLivro = getIntent().getIntExtra("idLivro", -1);
        String nomeUsuario = getIntent().getStringExtra("nome");

        if (!disponivel) {
            Toast.makeText(this, "Livro indisponível", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idLivro != -1) {
            DatabaseManager dbManager = new DatabaseManager(this);

            // Primeiro precisamos obter o ID do usuário baseado no nome
            int idUsuario = dbManager.obterIdUsuarioPorNome(nomeUsuario);

            if (idUsuario == -1) {
                Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Agora passamos ambos os IDs para o método
            boolean sucesso = dbManager.emprestarLivro(idLivro, idUsuario);

            if (sucesso) {
                disponivel = false;
                Toast.makeText(this, "Livro emprestado com sucesso!", Toast.LENGTH_SHORT).show();

                // Vai pra tela de impressão
                Intent intent = new Intent(PegarLivroActivity.this, Printer.class);
                intent.putExtra("nome", nomeUsuario);
                intent.putExtra("setor", getIntent().getStringExtra("setor"));
                intent.putExtra("disponivel", false);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao emprestar livro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Livro não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void emprestarLivroTela() {
        int idLivro = getIntent().getIntExtra("idLivro", -1);
        String nomeUsuario = getIntent().getStringExtra("nome");

        if (!disponivel) {
            Toast.makeText(this, "Livro indisponível", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idLivro != -1) {
            DatabaseManager dbManager = new DatabaseManager(this);

            // Primeiro precisamos obter o ID do usuário baseado no nome
            int idUsuario = dbManager.obterIdUsuarioPorNome(nomeUsuario);

            if (idUsuario == -1) {
                Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Agora passamos ambos os IDs para o método
            boolean sucesso = dbManager.emprestarLivro(idLivro, idUsuario);

            if (sucesso) {
                disponivel = false;
                Toast.makeText(this, "Livro emprestado com sucesso!", Toast.LENGTH_SHORT).show();

                // Vai pra tela de impressão
                Intent intent = new Intent(PegarLivroActivity.this, CupomNaTelaEmprestimo.class);
                intent.putExtra("nome", nomeUsuario);
                intent.putExtra("setor", getIntent().getStringExtra("setor"));
                intent.putExtra("disponivel", false);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao emprestar livro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Livro não encontrado", Toast.LENGTH_SHORT).show();
        }
    }
}


