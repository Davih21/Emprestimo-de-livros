package com.exampledv.livraria.ui.auth;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exampledv.livraria.DevolverLivroActivity;
import com.exampledv.livraria.FullScreenActivity;
import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;

public class Login3Activity extends FullScreenActivity {
    private EditText etEmail, etSenha;
    private Button btnLogin;
    private DatabaseManager dbManager;
    private int idLivro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);
        dbManager = new DatabaseManager(this);


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
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String setor = cursor.getString(cursor.getColumnIndexOrThrow("setor"));
            int idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

            cursor.close();

            if (dbEmail.equals(email) && dbSenha.equals(senha)) {
                if (tipo.equals("cliente")) {
                    // Verifica se o usuário tem livros emprestados
                    Cursor emprestimosCursor = dbManager.buscarLivrosEmprestadosPorUsuario(nome);

                    if (emprestimosCursor != null && emprestimosCursor.moveToFirst()) {
                        // Usuário tem livros emprestados - vai para tela de devolução
                        Intent intent = new Intent(this, DevolverLivroActivity.class);
                        intent.putExtra("idUsuario", idUsuario);
                        intent.putExtra("nome", nome);
                        intent.putExtra("setor", setor);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Você não tem livros para devolver", Toast.LENGTH_SHORT).show();
                    }

                    if (emprestimosCursor != null) {
                        emprestimosCursor.close();
                    }
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