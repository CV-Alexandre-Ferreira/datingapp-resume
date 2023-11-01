package com.example.datingappdev.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappdev.R;
import com.example.datingappdev.model.User;

import java.net.URI;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {
    private List<User> contatos;
    private Context context;

    public ChatsAdapter(List<User> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chats, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User usuario = contatos.get(position);
        boolean cabecalho = usuario.getEmail().isEmpty();

        holder.nome.setText(usuario.getName());
        holder.email.setText(usuario.getEmail());
        //holder.email.setVisibility(View.GONE);


    }


    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textChatName);
            email = itemView.findViewById(R.id.textChatEmail);
        }
    }
}
