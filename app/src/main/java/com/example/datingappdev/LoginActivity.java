package com.example.datingappdev;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.Base64Custom;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.User;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;

    private FirebaseAuth auth;

    private FirebaseUser usuarioAtual;

    private ValueEventListener listenerUsuarioAtual;

    private DatabaseReference usuariosRef;

    private User userAtual;



    private boolean didFirstSetUp = false; //(change later for database information)
    Button loginButton;

    private SignInButton googleSignInButton;

    TextView signUpText;

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initialConfig();

        usuariosRef = FirebaseConfig.getFirebaseDatabase().child("users");

        usuarioAtual = UserFirebase.getCurrentUser();

        userAtual = new User();

        auth = FirebaseConfig.getFirebaseAuthentication();

        if(auth.getCurrentUser()!= null)recuperarUsuarioDatabase();



        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserAuthentication();
            }
        });

        //configure the google Sign in
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //get default_web_client_id when setting up your sign in key for Google
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Google sign in Button
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //begin google sign in
                Log.d(TAG,"onClick: begin Google SignIn");
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN); //new we need to handle result of intent
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "onActivityResult: Google SignIn intent Result");
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                //google sign in success, now auth with firebase
                GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);

            }
            catch (Exception e){

                //failed google sign in
                Log.d(TAG, "onActivityResult ERROR: "+e.getMessage());
            }
        }
    }


    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "onSuccess: Logged In");

                        //get logged in user
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        //get user info
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        //check if user is new or existing
                        Log.d(TAG, "onSuccess: Email"+email);
                        Log.d(TAG, "onSuccess: UID"+uid);

                        if(authResult.getAdditionalUserInfo().isNewUser()){
                            //user is new - Account Created
                            Toast.makeText(LoginActivity.this, "Account Created\n"+email, Toast.LENGTH_SHORT).show();


                            User user = new User();
                            String idUser = Base64Custom.encodeBase64(email);
                            user.setId(idUser);
                            user.setName("");
                            user.setEmail(email);
                            user.setLatitude(0.0);
                            user.setLongitude(0.0);
                            user.setAge("0");
                            user.setGender("none");
                            user.setShowMe("none");

                            user.saveUserOnDatabase();
                            recuperarUsuarioDatabase();



                        }
                        else{
                            //existing user - Logged in
                            Log.d(TAG, "onSuccess: Existing user\n"+email);
                            Toast.makeText(LoginActivity.this, "Existing user\n"+email, Toast.LENGTH_SHORT).show();
                            recuperarUsuarioDatabase();
                        }

                        //start profile activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure: Logging failed "+e.getMessage());

                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            //openMainScreen();
            //recuperarUsuarioDatabase();
        }
    }

    public void logUser(User user){

        auth.signInWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(LoginActivity.this, "The user is now logged", Toast.LENGTH_SHORT).show();
                    recuperarUsuarioDatabase();


                }else{
                    //Depois colocar tratamentos de excessao
                    Toast.makeText(LoginActivity.this, "failure to authenticate", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void recuperarUsuarioDatabase(){

        listenerUsuarioAtual = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usuarioAtual = UserFirebase.getCurrentUser();

                for(DataSnapshot dados: snapshot.getChildren()){

                    User usuario = dados.getValue(User.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();


                    if(emailUsuarioAtual.equals(usuario.getEmail())){

                        userAtual = usuario;

                        Log.i("Entrouuu", "Entrou1:  " + userAtual.getEmail());
                        Log.i("Entrouuu", "Entrou2:  " + userAtual.getDidFirstSetUp());

                        openMainScreen();

                        break;

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openMainScreen(){

        Log.i("didFIRSTLogin", userAtual.getDidFirstSetUp().toString());
        if(userAtual.getDidFirstSetUp()){

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        }else {

            Intent intent = new Intent(LoginActivity.this, InitialSetUp.class);
            startActivity(intent);

        }

    }


    public void validateUserAuthentication(){

        String textEmail = loginEmail.getText().toString();
        String textPassword = loginPassword.getText().toString();

        if( !textEmail.isEmpty() && !textPassword.isEmpty()){

            User user = new User();
            user.setEmail(textEmail);
            user.setPassword(textPassword);

            logUser(user);

        }
        else{
            Toast.makeText(LoginActivity.this, "Submit all fields", Toast.LENGTH_SHORT).show();
        }

    }


    public void initialConfig(){
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText);
        googleSignInButton = findViewById(R.id.googleSignInButton);
    }
}