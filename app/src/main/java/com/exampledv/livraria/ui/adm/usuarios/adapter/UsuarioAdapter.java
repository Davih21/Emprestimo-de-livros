package com.exampledv.livraria.ui.adm.usuarios.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.exampledv.livraria.R;
import com.exampledv.livraria.ui.adm.usuarios.models.Usuario;

import java.util.ArrayList;


public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private Context context;
    private ArrayList<Usuario> usuarios;
    private OnUsuarioClickListener onUsuarioClickListener;

    // Interface para lidar com os cliques de editar e deletar
    public interface OnUsuarioClickListener {
        void onEditClick(Usuario usuario);
        void onDeleteClick(Usuario usuario);
    }

    // Passa a interface para o adapter
    public UsuarioAdapter(Context context, ArrayList<Usuario> usuarios, OnUsuarioClickListener onUsuarioClickListener) {
        this.context = context;
        this.usuarios = usuarios;
        this.onUsuarioClickListener = onUsuarioClickListener;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        holder.textViewNome.setText(usuario.getNome());
        holder.textViewSetor.setText(usuario.getSetor());
        holder.textViewEmail.setText(usuario.getEmail());
        holder.textViewTipo.setText(usuario.getTipo());

        // Configura os listeners de clique para editar e deletar
        holder.btnEditar.setOnClickListener(v -> onUsuarioClickListener.onEditClick(usuario));
        holder.btnDeletar.setOnClickListener(v -> onUsuarioClickListener.onDeleteClick(usuario));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewEmail, textViewTipo, textViewSetor;
        Button btnEditar, btnDeletar;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewSetor = itemView.findViewById(R.id.textViewSetor);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewTipo = itemView.findViewById(R.id.textViewTipo);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }
}
