package com.example.datingappdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.Base64Custom;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText nameField, passwordField, emailField;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initialConfig();

        //See if changes something on the database:
        auth = FirebaseConfig.getFirebaseAuthentication();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserRegister();
            }
        });
    }

    public void registerUser(User user){

        auth = FirebaseConfig.getFirebaseAuthentication();
        auth.createUserWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(SignUpActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                    UserFirebase.updateUserName(user.getName());


                    //finish();
                    try{

                        //Need to change to a different codification
                        String idUser = Base64Custom.encodeBase64(user.getEmail());
                        user.setId(idUser);
                        user.saveUserOnDatabase();

                    }catch (Exception e){e.printStackTrace();}
                }else{
                    String exception = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        exception = "Password not strong enough";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Email not valid";
                    }catch (FirebaseAuthUserCollisionException e){
                        exception = "This account is already registered";
                    }catch (Exception e){
                        exception = "Fail to register user: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(SignUpActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void validateUserRegister(){

        String textName = nameField.getText().toString();
        String textEmail = emailField.getText().toString();
        String textPassword = passwordField.getText().toString();

        if(!textName.isEmpty() && !textEmail.isEmpty() && !textPassword.isEmpty()){
            User user = new User();
            user.setName(textName);
            user.setEmail(textEmail);
            user.setPassword(textPassword);
            user.setLatitude(0.0);
            user.setLongitude(0.0);
            user.setAge("0");
            user.setGender("none");
            user.setShowMe("none");

            registerUser(user);
        }
        else{
            Toast.makeText(this, "Submit all data", Toast.LENGTH_SHORT).show();
        }


    }


    public void initialConfig(){
        nameField = findViewById(R.id.editTextName);
        passwordField = findViewById(R.id.editTextPassword);
        emailField = findViewById(R.id.editTextEmail);
        buttonRegister = findViewById(R.id.buttonRegister);
    }
}