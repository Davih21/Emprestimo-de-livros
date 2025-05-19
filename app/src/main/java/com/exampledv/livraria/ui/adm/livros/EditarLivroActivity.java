package com.exampledv.livraria.ui.adm.livros;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;


public class EditarLivroActivity extends AppCompatActivity {

    private EditText edtTitulo, edtAutor, edtImagemUrl; // Adicionando o campo para URL da imagem
    private CheckBox ckDisponivel;
    private Button btnSalvar;
    private DatabaseManager dbManager;
    private int livroId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_livro);

        // Inicializar as views
        edtTitulo = findViewById(R.id.edtTitulo);
        edtAutor = findViewById(R.id.edtAutor);
        edtImagemUrl = findViewById(R.id.edtImagemUrl); // Inicializando o campo de URL da imagem
        ckDisponivel = findViewById(R.id.ckDisponivel);
        btnSalvar = findViewById(R.id.btnSalvar);

        dbManager = new DatabaseManager(this);

        // Obter o ID do livro que será editado (passado via Intent)
        livroId = getIntent().getIntExtra("id", -1);

        // Carregar os dados do livro
        carregarLivro();

        // Definir o clique do botão salvar
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Atualizar o livro
                atualizarLivro();
            }
        });
    }

    @SuppressLint("Range")
    private void carregarLivro() {
        // Buscar os dados do livro no banco e preencher os campos de edição
        Cursor cursor = dbManager.buscarLivroPorId(livroId);
        if (cursor != null && cursor.moveToFirst()) {
            String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
            String autor = cursor.getString(cursor.getColumnIndex("autor"));
            int disponivel = cursor.getInt(cursor.getColumnIndex("disponivel"));
            String imagemUrl = cursor.getString(cursor.getColumnIndex("imagemUrl")); // Adicionando a URL da imagem

            edtTitulo.setText(titulo);
            edtAutor.setText(autor);
            ckDisponivel.setChecked(disponivel == 1); // Marcar como disponível se for 1
            edtImagemUrl.setText(imagemUrl); // Preencher o campo de URL da imagem
        }
    }

    private void atualizarLivro() {
        String titulo = edtTitulo.getText().toString();
        String autor = edtAutor.getText().toString();
        boolean disponivel = ckDisponivel.isChecked();
        String imagemUrl = edtImagemUrl.getText().toString(); // Captura a URL da imagem

        // Atualizar o livro no banco de dados
        boolean sucesso = dbManager.atualizarLivro(livroId, titulo, autor, disponivel, imagemUrl);
        if (sucesso) {
            finish(); // Fecha a atividade se a atualização for bem-sucedida
        } else {
            Toast.makeText(this, "Erro ao atualizar livro", Toast.LENGTH_SHORT).show();
        }
    }
}
