package com.example.datingappdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InitialActivity extends AppCompatActivity {

    private User userAtual;
    private ValueEventListener listenerUsuarioAtual;
    private DatabaseReference usuariosRef;
    private FirebaseAuth auth;
    private FirebaseUser usuarioAtual;
    private int controller = 0;

    public static final String TAG = "null TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        userAtual = new User();
        usuariosRef = FirebaseConfig.getFirebaseDatabase().child("users");
        auth = FirebaseConfig.getFirebaseAuthentication();
        usuarioAtual = UserFirebase.getCurrentUser();


        //if(auth.getCurrentUser()!= null)recuperarUsuarioDatabase();



    }

    @Override
    protected void onStart() {

        super.onStart();

        //auth.signOut();

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser != null) {
            //openMainScreen();
            Log.d(TAG, "TAG onStart IF ");
            recuperarUsuarioDatabase();
        }
        else {
            Log.d(TAG, "TAG onStart ELSE ");
            openLoginScreen();

        }

    }

    public void openMainScreen(){

        Log.i("emailUser", userAtual.getEmail().toString());
        Log.i("didFIRST", userAtual.getDidFirstSetUp().toString());

        if(userAtual.getDidFirstSetUp()){

            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
            startActivity(intent);


        }else {

            Intent intent = new Intent(InitialActivity.this, InitialSetUp.class);
            startActivity(intent);


        }

    }
    public void openLoginScreen(){

        Intent intent = new Intent(InitialActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    public void recuperarUsuarioDatabase(){

        Log.d(TAG, "TAG onDataChange recuperarUsuarioDatabase FIRST");

        listenerUsuarioAtual = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Log.d(TAG, "TAG onDataChange recuperarUsuarioDatabase ");

                for(DataSnapshot dados: snapshot.getChildren()){

                    User usuario = dados.getValue(User.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();

                    Log.d(TAG, "TAG onRecuperarUsuarioDatabase emailUsuarioAtual: " + emailUsuarioAtual);

                    if(emailUsuarioAtual.equals(usuario.getEmail())){

                        userAtual = usuario;

                        Log.d(TAG, "TAG onRecuperarUsuarioDatabase IF emailUsuarioAtual ");

                        if(controller == 0){
                            Log.d(TAG, "TAG onRecuperarUsuarioDatabase IF controller ");
                            openMainScreen();
                            controller++;
                        }


                        //Log.i("Entrouuu", "Entrou1:  " + userAtual.getEmail());
                        //Log.i("Entrouuu", "Entrou2:  " + userAtual.getDidFirstSetUp());

                        break;

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}