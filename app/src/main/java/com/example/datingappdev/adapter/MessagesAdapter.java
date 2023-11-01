package com.example.datingappdev.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappdev.R;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<Message> mensagens;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MessagesAdapter(List<Message> lista, Context c) {
        this.mensagens = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;
        if(viewType == TIPO_REMETENTE){

            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_sender, parent, false);

        }else if (viewType == TIPO_DESTINATARIO){

            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_message_receiver, parent, false);
        }

        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Message mensagem = mensagens.get(position);
        String msg = mensagem.getMessage();


            holder.mensagem.setText(msg);

            String nome = mensagem.getName();
            if(!nome.isEmpty()) {
                holder.nome.setText(nome);
            }else{holder.nome.setVisibility(View.GONE);}


    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message mensagem = mensagens.get(position);
        String idUsuario = UserFirebase.getUserId();

        if(idUsuario.equals(mensagem.getUserId())) {
            return TIPO_REMETENTE;
        }
        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mensagem;
        TextView nome;
        public MyViewHolder(View itemView){
            super(itemView);

            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            nome = itemView.findViewById(R.id.textNomeExibicao);
        }
    }

}
