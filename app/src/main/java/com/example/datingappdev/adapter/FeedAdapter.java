package com.example.datingappdev.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappdev.R;
import com.example.datingappdev.model.User;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {
    private List<User> contatos;
    private Context context;

    public FeedAdapter(List<User> listaContatos, Context c) {
        this.contatos = listaContatos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User usuario = contatos.get(position);
        boolean cabecalho = usuario.getEmail().isEmpty();


        holder.nome.setText(usuario.getName());

        if(usuario.getDateFirst()) holder.dfButton.setVisibility(View.VISIBLE);
        if(usuario.getSexNow()) holder.snButton.setVisibility(View.VISIBLE);
        if(usuario.getMoney()) holder.moneyButton.setVisibility(View.VISIBLE);
        if(usuario.getRace()) holder.colorButton.setVisibility(View.VISIBLE);
        if(usuario.getTall()) holder.tallButton.setVisibility(View.VISIBLE);
        if(usuario.getHouse()) holder.houseButton.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        Button snButton, dfButton, moneyButton, colorButton, tallButton, houseButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textFeedName);
            snButton = itemView.findViewById(R.id.snButton);
            dfButton = itemView.findViewById(R.id.dfButton);
            moneyButton = itemView.findViewById(R.id.moneyButton);
            colorButton = itemView.findViewById(R.id.colorButton);
            tallButton = itemView.findViewById(R.id.tallButton);
            houseButton = itemView.findViewById(R.id.houseButton);


        }
    }
}
