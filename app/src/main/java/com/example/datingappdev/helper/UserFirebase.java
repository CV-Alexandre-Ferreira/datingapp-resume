package com.example.datingappdev.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserFirebase {

    public static String getUserId(){
        FirebaseAuth user = FirebaseConfig.getFirebaseAuthentication();
        String email = user.getCurrentUser().getEmail();

        //Fazer a codificação aqui
        String identificadorUsuario = Base64Custom.encodeBase64(email);
        return identificadorUsuario;

        //return email;
    }

    public static FirebaseUser getCurrentUser(){
        FirebaseAuth user = FirebaseConfig.getFirebaseAuthentication();
        return user.getCurrentUser();
    }

    public static Boolean updateUserName(String nome){

        try{
            FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });
            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static User getDataFromLoggedUser(){
        FirebaseUser firebaseUser = getCurrentUser();
        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());

        return user;

    }

}
