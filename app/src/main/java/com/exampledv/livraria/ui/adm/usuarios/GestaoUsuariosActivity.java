//package com.thiago.book.ui.adm.usuarios;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.thiago.book.R;
//import com.thiago.book.database.DatabaseManager;
//
//import java.util.ArrayList;
//
//public class GestaoUsuariosActivity extends AppCompatActivity {
//
//    private ListView listViewUsuarios;
//    private Button btnAdicionarUsuario;
//    private DatabaseManager dbManager;
//
//    private static final String TAG = "GestaoUsuariosActivity";  // Defina a tag para os logs
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gestao_usuarios);
//
//        listViewUsuarios = findViewById(R.id.listViewUsuarios);
//        btnAdicionarUsuario = findViewById(R.id.btnAdicionarUsuario);
//
//        dbManager = new DatabaseManager(this);
//
//        // Carregando usuários ao iniciar
//        carregarUsuarios();
//
//        btnAdicionarUsuario.setOnClickListener(v -> {
//            Intent intent = new Intent(GestaoUsuariosActivity.this, AdicionarUsuarioActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Atualiza os dados da lista quando a tela for retomada
//        carregarUsuarios();
//    }
//
//    @SuppressLint("Range")
//    private void carregarUsuarios() {
//        // Atualiza a lista de usuários no ListView
//        Cursor cursor = dbManager.buscarUsuarios();
//
//        if (cursor != null) {
//            // Criar uma lista para armazenar os itens
//            ArrayList<String> usuariosList = new ArrayList<>();
//
//            // Loop através dos itens do cursor e adicionar à lista
//            if (cursor.moveToFirst()) {
//                do {
//                    String nome = cursor.getString(cursor.getColumnIndex("nome"));
//                    String email = cursor.getString(cursor.getColumnIndex("email"));
//                    String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
//
//                    // Logando as informações para depuração
//                    Log.d(TAG, "Nome: " + nome + ", Email: " + email + ", Tipo: " + tipo);
//
//                    // Adicionando à lista que será usada no ListView
//                    usuariosList.add("Nome: " + nome + "\nEmail: " + email + "\nTipo: " + tipo);
//                } while (cursor.moveToNext());
//            }
//
//            // Criar o adapter para exibir os dados no ListView
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                    this,
//                    android.R.layout.simple_list_item_1,  // Layout simples de item de lista
//                    usuariosList
//            );
//
//            // Definir o adapter no ListView
//            listViewUsuarios.setAdapter(adapter);
//
//            cursor.close();  // Fechar o cursor após o uso
//        } else {
//            Log.d(TAG, "Nenhum usuário encontrado.");
//        }
//    }
//}
package com.exampledv.livraria.ui.adm.usuarios;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.exampledv.livraria.R;
import com.exampledv.livraria.database.DatabaseManager;
import com.exampledv.livraria.ui.adm.usuarios.adapter.UsuarioAdapter;
import com.exampledv.livraria.ui.adm.usuarios.models.Usuario;

import java.util.ArrayList;


public class GestaoUsuariosActivity extends AppCompatActivity implements UsuarioAdapter.OnUsuarioClickListener {

    private RecyclerView recyclerViewUsuarios;
    private Button btnAdicionarUsuario;
    private DatabaseManager dbManager;
    private static final String TAG = "GestaoUsuariosActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestao_usuarios);

        recyclerViewUsuarios = findViewById(R.id.recyclerViewUsuarios);
        btnAdicionarUsuario = findViewById(R.id.btnAdicionarUsuario);
        dbManager = new DatabaseManager(this);

        // Configura o RecyclerView
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        // Carregar usuários ao iniciar
        carregarUsuarios();

        btnAdicionarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(GestaoUsuariosActivity.this, AdicionarUsuarioActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarUsuarios();
    }

    @SuppressLint("Range")
    void carregarUsuarios() {
        Cursor cursor = dbManager.buscarUsuarios();

        if (cursor != null) {
            ArrayList<Usuario> usuariosList = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id")); // Pega o ID do usuário
                    String nome = cursor.getString(cursor.getColumnIndex("nome"));
                    String setor = cursor.getString(cursor.getColumnIndex("setor"));
                    String email = cursor.getString(cursor.getColumnIndex("email"));
                    String tipo = cursor.getString(cursor.getColumnIndex("tipo"));

                    usuariosList.add(new Usuario(id, nome, setor, email, tipo));
                } while (cursor.moveToNext());
            }

            // Atualiza o RecyclerView com a lista de usuários
            UsuarioAdapter adapter = new UsuarioAdapter(this, usuariosList, this);
            recyclerViewUsuarios.setAdapter(adapter);

            cursor.close();
        } else {
            Log.d(TAG, "Nenhum usuário encontrado.");
        }
    }


    @Override
    public void onEditClick(Usuario usuario) {

        Intent intent = new Intent(this, EditarUsuarioActivity.class);

        // Passa os dados do usuário selecionado para a nova Activity
        intent.putExtra("id", usuario.getId());
        intent.putExtra("nome", usuario.getNome());
        intent.putExtra("setor", usuario.getSetor());
        intent.putExtra("email", usuario.getEmail());
        intent.putExtra("tipo", usuario.getTipo());


        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Usuario usuario) {
        // Aqui você pode deletar o usuário do banco de dados e atualizar a lista
        boolean deleted = dbManager.deletarUsuario(usuario.getId());
        if (deleted) {
            Toast.makeText(this, "Usuário deletado com sucesso", Toast.LENGTH_SHORT).show();
            carregarUsuarios();  // Atualiza a lista após a exclusão
        } else {
            Toast.makeText(this, "Erro ao deletar o usuário", Toast.LENGTH_SHORT).show();
        }

}
}
