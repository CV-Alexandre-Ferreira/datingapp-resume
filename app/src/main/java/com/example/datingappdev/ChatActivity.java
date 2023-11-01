package com.example.datingappdev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.datingappdev.adapter.MessagesAdapter;
import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.Base64Custom;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.Chat;
import com.example.datingappdev.model.Message;
import com.example.datingappdev.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewNome;
    //private CircleImageView circleImageViewFoto;
    private User usuarioDestinatario;
    private ImageView imageCamera;

    private User usuarioRemetente;
    //private AppBarConfiguration appBarConfiguration;
    //private ActivityChatBinding binding;
    private ImageView imagemContatoConversa;
    private EditText editMensagem;
    private RecyclerView recyclerMensagens;
    private MessagesAdapter adapter;

    private List<Message> mensagens = new ArrayList<>();

    private static final int SELECAO_CAMERA = 100;

    private DatabaseReference database;
    private StorageReference storage;

    //identificador usuarios remetente e destinatario
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idUsuarioRemetente = UserFirebase.getUserId();
        usuarioRemetente = UserFirebase.getDataFromLoggedUser();

        //binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_chat);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewNome = findViewById(R.id.textViewNomeChat);
        editMensagem = findViewById(R.id.editMensagem);
        recyclerMensagens = findViewById(R.id.recyclerMensagens);


        //Recuperar dados do usuario destinatario
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

                usuarioDestinatario = (User) bundle.getSerializable("chatContato");

                toolbar.setTitle(usuarioDestinatario.getName());

            //recuperar dados usuario destinatario
                idUsuarioDestinatario = Base64Custom.encodeBase64(usuarioDestinatario.getEmail());
                /*************/


        }

        //Configuraçao adapater
        adapter = new MessagesAdapter(mensagens, getApplicationContext());

        //Configuraçao recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMensagens.setLayoutManager(layoutManager);
        recyclerMensagens.setHasFixedSize(true);
        recyclerMensagens.setAdapter(adapter);

        database = FirebaseConfig.getFirebaseDatabase();
        storage = FirebaseConfig.getFirebaseStorage();
        mensagensRef = database.child("mensagens")
                .child( idUsuarioRemetente )
                .child( idUsuarioDestinatario );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try{
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                }

                if(imagem != null){

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();

                    //Configurar referencia firebase
                    final StorageReference imagemRef = storage.child("imagens")
                            .child("fotos")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro ao fazer upload");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String downloadUrl = task.getResult().toString();

                                    if(usuarioDestinatario != null){//mensagem normal

                                        Message message = new Message();
                                        message.setUserId(idUsuarioRemetente);
                                        message.setMessage("imagem.jpeg");

                                        //salvar mensagem remetente
                                        salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, message);
                                        //salvar mensagem destinatário
                                        salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, message);

                                    }

                                    Toast.makeText(ChatActivity.this, "Sucesso ao enviar imagem", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });

                }
            }catch (Exception e){e.printStackTrace();}
        }
    }

    public void enviarMensagem(View view){
        String textoMensagem = editMensagem.getText().toString();
        if(!textoMensagem.isEmpty()){

            if(usuarioDestinatario != null){

                Message mensagem = new Message();
                mensagem.setUserId( idUsuarioRemetente );
                mensagem.setMessage(textoMensagem);

                //Salvar mensagem para o remetente
                salvarMensagem( idUsuarioRemetente,idUsuarioDestinatario, mensagem );

                //Salvar mensagem para o destinatário
                salvarMensagem( idUsuarioDestinatario,idUsuarioRemetente, mensagem );

                //Salva conversa remetente
                salvarConversa(idUsuarioRemetente, idUsuarioDestinatario, usuarioDestinatario, mensagem, false);

                //Salva conversa destinatario
                salvarConversa(idUsuarioDestinatario, idUsuarioRemetente, usuarioRemetente, mensagem, false);

            }

        }
    }

    private void salvarConversa(String idRemetente, String idDestinatario, User usuarioExibicao, Message msg, boolean isGroup){
        Chat conversaRemetente = new Chat();
        conversaRemetente.setIdSender(idRemetente);
        conversaRemetente.setIdReceiver(idDestinatario);
        conversaRemetente.setLastMessage(msg.getMessage());


            //Conversa convencional
            conversaRemetente.setShowingUser(usuarioExibicao);

            conversaRemetente.save();




    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Message msg) {
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);

        //Limpar texto
        editMensagem.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarMensagens();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener(childEventListenerMensagens);
    }

    private void recuperarMensagens(){

        mensagens.clear();

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message mensagem = snapshot.getValue(Message.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}