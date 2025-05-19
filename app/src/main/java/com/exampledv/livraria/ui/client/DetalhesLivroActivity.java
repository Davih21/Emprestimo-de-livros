package com.exampledv.livraria.ui.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exampledv.livraria.FullScreenActivity;
import com.exampledv.livraria.R;
import com.exampledv.livraria.ui.auth.Login2Activity;


public class DetalhesLivroActivity extends FullScreenActivity {

    private ImageView dt_fotoLivro;
    private TextView dt_nomeLivro, dt_autorLivro, txtDisponibilidade;
    private Button bt_solicitarLivro;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_livro);

        dt_fotoLivro = findViewById(R.id.dt_fotoLivro);
        dt_nomeLivro = findViewById(R.id.dt_nomeLivro);
        dt_autorLivro = findViewById(R.id.dt_autorLivro);
        txtDisponibilidade = findViewById(R.id.txtDisponibilidade);
        bt_solicitarLivro = findViewById(R.id.bt_solicitarLivro);

        int idLivro = getIntent().getIntExtra("idLivro", -1);  // <- ESSENCIAL
        String imgLivro = getIntent().getExtras().getString("imgLivro");
        String titulo = getIntent().getExtras().getString("titulo");
        String autor = getIntent().getExtras().getString("autor");
        boolean disponivel = getIntent().getBooleanExtra("disponivel", false);

        if (disponivel) {
            txtDisponibilidade.setText("Disponível");
            txtDisponibilidade.setBackgroundResource(R.drawable.bg_status_disponivel);
        } else {
            txtDisponibilidade.setText("Emprestado");
            txtDisponibilidade.setBackgroundResource(R.drawable.bg_status_emprestado);
        }

        Glide.with(getApplicationContext()).load(imgLivro).into(dt_fotoLivro);
        dt_nomeLivro.setText(titulo);
        dt_autorLivro.setText(autor);

        bt_solicitarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalhesLivroActivity.this, Login2Activity.class);

                // Passando os dados para a próxima tela
                intent.putExtra("idLivro", idLivro); // agora vai funcionar
                intent.putExtra("imgLivro", imgLivro);
                intent.putExtra("titulo", titulo);
                intent.putExtra("autor", autor);
                intent.putExtra("disponivel", disponivel);

                startActivity(intent);
            }
        });
    }
}
