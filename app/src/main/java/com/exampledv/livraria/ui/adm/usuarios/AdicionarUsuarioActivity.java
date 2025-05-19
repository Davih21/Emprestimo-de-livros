package com.exampledv.livraria.ui.adm.usuarios;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;


public class AdicionarUsuarioActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmail, editTextSenha, editTextSetor;
    private Spinner spinnerTipo;
    private Button btnSalvar;
    private DatabaseManager dbManager;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_usuario);

        editTextNome = findViewById(R.id.editTextNome);
        editTextSetor = findViewById(R.id.editTextSetor);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnSalvar = findViewById(R.id.btnSalvar);
        dbManager = new DatabaseManager(this);

        // Criando o adapter para o spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[] {"admin", "cliente"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        btnSalvar.setOnClickListener(v -> {
            String nome = editTextNome.getText().toString();
            String setor = editTextSetor.getText().toString();
            String email = editTextEmail.getText().toString();
            String senha = editTextSenha.getText().toString();
            String tipo = spinnerTipo.getSelectedItem().toString(); // Pegando o tipo selecionado no Spinner

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }

            long resultado = dbManager.adicionarUsuario(nome, setor,email, senha, tipo);

            // Adicionando log aqui para verificar se o usuário foi salvo
            if (resultado != -1) {
                Log.d("AdicionarUsuarioActivity", "Usuário adicionado com sucesso! ID: " + resultado);
                Toast.makeText(this, "Usuário adicionado com sucesso", Toast.LENGTH_SHORT).show();
                finish();  // Volta para a tela anterior

                // Atualiza a lista de usuários na tela anterior
                Intent intent = new Intent(AdicionarUsuarioActivity.this, GestaoUsuariosActivity.class);
                startActivity(intent);
            } else {
                Log.e("AdicionarUsuarioActivity", "Erro ao adicionar usuário");
                Toast.makeText(this, "Erro ao adicionar usuário", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
