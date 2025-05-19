package com.exampledv.livraria.ui.adm.livros;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


import java.util.Objects;

public class AdicionarLivroActivity extends AppCompatActivity {

    private TextInputEditText etBookTitle, etBookAuthor, etBookImageUrl;
    private MaterialButton btnSave;
    private DatabaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_livro);

        // Inicializações
        etBookTitle = findViewById(R.id.etBookTitle);
        etBookAuthor = findViewById(R.id.etBookAuthor);
        etBookImageUrl = findViewById(R.id.etBookImageUrl);  // Novo campo para URL da imagem
        btnSave = findViewById(R.id.btnSave);
        dbManager = new DatabaseManager(this);


        btnSave.setOnClickListener(view -> {
            String title = Objects.requireNonNull(etBookTitle.getText()).toString().trim();
            String author = Objects.requireNonNull(etBookAuthor.getText()).toString().trim();
            String imageUrl = Objects.requireNonNull(etBookImageUrl.getText()).toString().trim();  // URL da imagem

            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chamando o método adicionarLivro com o parâmetro 'disponivel' como 'true'
            long result = dbManager.adicionarLivro(title, author, true, imageUrl);  // Passando a URL da imagem
            if (result > 0) {
                Toast.makeText(this, "Livro adicionado com sucesso", Toast.LENGTH_SHORT).show();
                finish(); // Fecha a atividade
            } else {
                Toast.makeText(this, "Erro ao adicionar livro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
