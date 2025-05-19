package com.exampledv.livraria.ui.auth;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.exampledv.livraria.FullScreenActivity;
import com.exampledv.livraria.PegarLivroActivity;
import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;
import com.exampledv.livraria.ui.adm.AdminActivity;
import com.exampledv.livraria.ui.adm.emprestimo.models.Emprestimo;
import com.exampledv.livraria.ui.client.ClienteActivity;

public class Login2Activity extends FullScreenActivity {
    private EditText etEmail, etSenha;
    private Button btnLogin;
    private DatabaseManager dbManager;

    // Variáveis para armazenar os dados do livro
    private String imgLivro;
    private String titulo;
    private String autor;
    private boolean disponivel;
    private int idLivro; // <-- Adiciona aqui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        dbManager = new DatabaseManager(this);

        //-------------------------------------------------------------

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            imgLivro = extras.getString("imgLivro");
            titulo = extras.getString("titulo");
            autor = extras.getString("autor");
            disponivel = extras.getBoolean("disponivel", false);
            idLivro = extras.getInt("idLivro", -1); // <- pega o idLivro
        }


        //-----------------------------------------------------------------


        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String senha = etSenha.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            autenticarUsuario(email, senha);
        });
    }


    private void autenticarUsuario(String email, String senha) {
        Cursor cursor = dbManager.buscarUsuarioPorEmail(email);

        if (cursor != null && cursor.moveToFirst()) {
            String dbEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String dbSenha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));

            // Pegando nome e setor antes de fechar o cursor
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String setor = cursor.getString(cursor.getColumnIndexOrThrow("setor"));

            cursor.close();

            if (dbEmail.equals(email) && dbSenha.equals(senha)) {
                if (tipo.equals("cliente")) {
                    Toast.makeText(this, "Bem vindo cliente", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, PegarLivroActivity.class);

                    intent.putExtra("idLivro", idLivro); // <- Passando o idLivro aqui também
                    intent.putExtra("imgLivro", imgLivro);
                    intent.putExtra("titulo", titulo);
                    intent.putExtra("autor", autor);
                    intent.putExtra("disponivel", disponivel);
                    intent.putExtra("nome", nome);
                    intent.putExtra("setor", setor);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Essa conta é inválida aqui!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Email ou senha inválidos!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Email ou senha inválidos!", Toast.LENGTH_SHORT).show();
        }
    }

}