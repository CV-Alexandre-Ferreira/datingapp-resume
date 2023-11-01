package com.example.datingappdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.Base64Custom;
import com.example.datingappdev.model.User;
import com.google.firebase.auth.FirebaseAuth;

public class CardActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private CheckBox checkBoxHouse, checkBoxRace, checkBoxSexNow, checkBoxDateFirst, checkBoxTall, checkBoxMoney;
    private Button buttonSaveSetUp;
    private TextView textViewDistance;

    private SeekBar seekBarDistance;

    private FirebaseAuth authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_set_up);
        initialConfig();

        authUser = FirebaseConfig.getFirebaseAuthentication();


        buttonSaveSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                User user = new User();
                String email = authUser.getCurrentUser().getEmail();
                user.setEmail(email);
                String idUser = Base64Custom.encodeBase64(user.getEmail());
                user.setId(idUser);


                if(checkBoxSexNow.isChecked()) user.setSexNow(true);
                if(checkBoxDateFirst.isChecked()) user.setDateFirst(true);
                if(checkBoxHouse.isChecked()) user.setHouse(true);
                if(checkBoxRace.isChecked()) user.setRace(true);
                if(checkBoxTall.isChecked()) user.setTall(true);
                if(checkBoxMoney.isChecked()) user.setMoney(true);


                user.setDidFirstSetUp(true);
                user.setDistanceOfInterest(seekBarDistance.getProgress());
                //user.saveUserOnDatabase();
                user.update();

                Intent i = new Intent(CardActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        seekBarDistance.setMax(100);


        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                textViewDistance.setText(String.valueOf(progress) + " KM");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void initialConfig(){
        checkBoxDateFirst = findViewById(R.id.checkBoxDateFirst);
        checkBoxHouse = findViewById(R.id.checkboxHouse);
        checkBoxMoney = findViewById(R.id.checkBoxMoney);
        checkBoxRace = findViewById(R.id.checkBoxRace);
        checkBoxSexNow = findViewById(R.id.checkBoxSexNow);
        checkBoxTall = findViewById(R.id.checkBoxTall);
        buttonSaveSetUp = findViewById(R.id.buttonSaveSetUp);
        seekBarDistance = findViewById(R.id.seekBarDistance);
        textViewDistance = findViewById(R.id.textViewDistance);

    }

}