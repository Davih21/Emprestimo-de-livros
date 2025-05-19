package com.exampledv.livraria.ui.auth;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;
import com.exampledv.livraria.ui.adm.AdminActivity;
import com.exampledv.livraria.ui.client.ClienteActivity;


public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnLogin;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        // Utiliza a função buscarUsuarioPorEmail para evitar o loop desnecessário
        Cursor cursor = dbManager.buscarUsuarioPorEmail(email);

        if (cursor != null && cursor.moveToFirst()) {
            String dbEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String dbSenha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));

            cursor.close();

            // Verifica a senha
            if (dbEmail.equals(email) && dbSenha.equals(senha)) {
                Toast.makeText(this, "Login como " + tipo, Toast.LENGTH_SHORT).show();
                Intent intent;
                if (tipo.equals("admin")) {
                    intent = new Intent(this, AdminActivity.class);
                } else {
                    intent = new Intent(this, ClienteActivity.class);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email ou senha inválidos!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Caso o cursor esteja nulo ou não encontre o email
            Toast.makeText(this, "Email ou senha inválidos!", Toast.LENGTH_SHORT).show();
        }
    }
}
