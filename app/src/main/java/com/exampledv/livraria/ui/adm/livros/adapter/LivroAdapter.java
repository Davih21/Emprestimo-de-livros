package com.exampledv.livraria.ui.adm.livros.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exampledv.livraria.R;
import com.exampledv.livraria.ui.adm.livros.models.Livro;


import java.util.List;

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.LivroViewHolder> {

    private final Context context;
    private final List<Livro> livros;
    private final OnLivroClickListener listener;

    public interface OnLivroClickListener {
        void onEditClick(Livro livro);
        void onDeleteClick(Livro livro);
        void onItemClick(Livro livro);
    }

    public LivroAdapter(Context context, List<Livro> livros, OnLivroClickListener listener) {
        this.context = context;
        this.livros = livros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LivroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_livro, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivroViewHolder holder, int position) {
        Livro livro = livros.get(position);
        holder.titulo.setText(livro.getTitulo());
        holder.autor.setText(livro.getAutor());

        if (livro.isDisponivel()) {
            holder.status.setText("Disponível");
            holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark)); // Verde para disponível
        } else {
            holder.status.setText("Emprestado");
            holder.status.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark)); // Vermelho para emprestado
        }

        String imagemUrl = livro.getImagemUrl();
        Log.d("LivroAdapter", "Imagem URL: " + imagemUrl);

        if (imagemUrl != null && !imagemUrl.isEmpty()) {
            Glide.with(context)
                    .load(imagemUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(holder.imgLivro);
        } else {
            holder.imgLivro.setImageResource(R.drawable.ic_placeholder);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(livro));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(livro));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(livro));
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    static class LivroViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, autor, status;
        ImageView imgLivro;
        Button btnEdit, btnDelete;

        public LivroViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.txtTituloLivro);
            autor = itemView.findViewById(R.id.txtAutorLivro);
            status = itemView.findViewById(R.id.txtDisponibilidadeLivro);
            imgLivro = itemView.findViewById(R.id.imgLivro);
            btnEdit = itemView.findViewById(R.id.btnEditarLivro);
            btnDelete = itemView.findViewById(R.id.btnExcluirLivro);
        }
    }
}
