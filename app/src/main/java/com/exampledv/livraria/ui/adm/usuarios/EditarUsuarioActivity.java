package com.exampledv.livraria.ui.adm.usuarios;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;


public class EditarUsuarioActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextSenha, editTextTipo, editTextSetor;
    private Button btnSalvar;
    private DatabaseManager dbManager;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        editTextNome = findViewById(R.id.editTextNome);
        editTextSetor = findViewById(R.id.editTextSetor);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        editTextTipo = findViewById(R.id.editTextTipo);
        btnSalvar = findViewById(R.id.btnSalvar);

        dbManager = new DatabaseManager(this);

        // Receber os dados do usuário enviado pela Intent
        usuarioId = getIntent().getIntExtra("id", -1);
        String nome = getIntent().getStringExtra("nome");
        String setor = getIntent().getStringExtra("setor");
        String email = getIntent().getStringExtra("email");
        String senha = getIntent().getStringExtra("senha");
        String tipo = getIntent().getStringExtra("tipo");

        // Preencher os campos com os dados recebidos
        editTextNome.setText(nome);
        editTextSetor.setText(setor);
        editTextEmail.setText(email);
        editTextSenha.setText(senha);
        editTextTipo.setText(tipo);

        btnSalvar.setOnClickListener(v -> salvarEdicao());

    }

    private void salvarEdicao() {
        String nome = editTextNome.getText().toString();
        String setor = editTextSetor.getText().toString();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();
        String tipo = editTextTipo.getText().toString();

        if (dbManager.atualizarUsuario(usuarioId, nome, setor, email, senha, tipo)) {
            Toast.makeText(this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a Activity e retorna
        } else {
            Toast.makeText(this, "Erro ao atualizar o usuário.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
