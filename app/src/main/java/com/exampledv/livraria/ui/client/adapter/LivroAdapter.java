package com.exampledv.livraria.ui.client.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exampledv.livraria.R;
import com.exampledv.livraria.ui.adm.livros.models.Livro;
import com.exampledv.livraria.ui.client.DetalhesLivroActivity;


import java.util.List;

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.LivroViewHolder> {

    private List<Livro> livroList;
    private OnItemClickListener onItemClickListener;

    private Context context;
    private boolean isAdmin;

    public LivroAdapter(Context context, List<Livro> livroList, OnItemClickListener onItemClickListener, boolean isAdmin) {
        this.context = context;
        this.livroList = livroList;
        this.onItemClickListener = onItemClickListener;
        this.isAdmin = isAdmin;
    }


    @Override
    public LivroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livro_holder, parent, false);
        return new LivroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LivroViewHolder holder, int position) {
        Livro livro = livroList.get(position);
        holder.bind(livro);

    }

    @Override
    public int getItemCount() {
        return livroList.size();
    }

    public class LivroViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitulo, txtAutor, txtDisponibilidade;
        private ImageView imgLivro;

        public LivroViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtAutor = itemView.findViewById(R.id.txtAutor);
            imgLivro = itemView.findViewById(R.id.imgLivro);
            txtDisponibilidade = itemView.findViewById(R.id.txtDisponibilidade);


        }

        public void bind(Livro livro) {
            txtTitulo.setText(livro.getTitulo());
            txtAutor.setText(livro.getAutor());


            if (livro.isDisponivel()) {
                txtDisponibilidade.setText("Disponível");
                txtDisponibilidade.setBackgroundResource(R.drawable.bg_status_disponivel);
            } else {
                txtDisponibilidade.setText("Emprestado");
                txtDisponibilidade.setBackgroundResource(R.drawable.bg_status_emprestado);
            }

            // Carregando a imagem usando Glide (ou Picasso)
            Glide.with(itemView.getContext())
                    .load(livro.getImagemUrl()) // Supondo que 'livro.getImagemUrl()' seja a URL da imagem
                    .into(imgLivro);

            itemView.setOnClickListener(view -> {
                if (isAdmin) {

                    onItemClickListener.onItemClick(livro);
                } else {
                    Intent intent = new Intent(context, DetalhesLivroActivity.class);
                    intent.putExtra("idLivro", livro.getId());
                    intent.putExtra("imgLivro", livro.getImagemUrl());
                    intent.putExtra("titulo", livro.getTitulo());
                    intent.putExtra("autor", livro.getAutor());
                    intent.putExtra("disponivel", livro.isDisponivel());
                    context.startActivity(intent);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Livro livro);
    }
}
