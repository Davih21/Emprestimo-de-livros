package com.exampledv.livraria.ui.adm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.exampledv.livraria.R;
import com.exampledv.livraria.ui.adm.livros.GestaoLivrosActivity;
import com.exampledv.livraria.ui.adm.usuarios.GestaoUsuariosActivity;


public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void onCardClick(View view) {
        Intent intent = null;


        if (view.getId() == R.id.cardGestaoUsuarios) {
            intent = new Intent(this, GestaoUsuariosActivity.class);
        } else if (view.getId() == R.id.cardGestaoLivros) {
            intent = new Intent(this, GestaoLivrosActivity.class);
        }


        if (intent != null) {
            startActivity(intent);
        }
    }
}
