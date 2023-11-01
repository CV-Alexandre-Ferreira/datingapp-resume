package com.example.datingappdev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datingappdev.model.User;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class InitialSetUp extends IntroActivity {

    private EditText editFirstName, editAge;
    private String textFirstName, textAge;
    private Button buttonGenderMan, buttonGenderWoman, buttonGenderOther, buttonShowMeMen, buttonShowMeWomen, buttonShowMeEveryone,
            buttonAtColor, buttonAtTall, buttonAtHouse, buttonAtMoney, buttonAtDF;

     private User user;

    private Boolean colorIsClicked = false, tallIsClicked = false,
            houseIsClicked = false, moneyIsClicked = false,DFisClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_initial_set_up);

        user = new User();
        editFirstName = findViewById(R.id.editFirstName);

        setButtonBackVisible(false);
        setButtonNextVisible(true);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_welcome)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.fragment_slide_name)
                .build());
        initialConfig();
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_age)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_gender)
                //.canGoForward(false)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_show_me)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_attributes)
                .canGoForward(false)
                .build());


        setNavigationPolicy(new NavigationPolicy() {
            @Override public boolean canGoForward(int position) {
                editFirstName = findViewById(R.id.editFirstName);
                editAge = findViewById(R.id.editAge);
                if(position == 1) {
                    if(editFirstName.getText().toString().equals("Name")) return false;
                    else{
                        textFirstName = editFirstName.getText().toString();
                    }
                }
                else if(position == 2) {
                    if(editAge.getText().toString().equals("Age")) return false;
                    else {
                        textAge = editAge.getText().toString();
                    }
                }
                return true;
            }


            @Override public boolean canGoBackward(int position) {
                return false;
            }
        });

    }

    public void testButtonGenderMan(View view){
        setButtonsGender();
        buttonGenderMan.setBackgroundColor( getResources().getColor(R.color.teal_700));
        buttonGenderWoman.setBackgroundColor( getResources().getColor(R.color.teal_200));
        buttonGenderOther.setBackgroundColor( getResources().getColor(R.color.teal_200));

        user.setGender("man");

    }
    public void testButtonGenderWoman(View view){
        setButtonsGender();
        buttonGenderMan.setBackgroundColor( getResources().getColor(R.color.teal_200));
        buttonGenderWoman.setBackgroundColor( getResources().getColor(R.color.teal_700));
        buttonGenderOther.setBackgroundColor( getResources().getColor(R.color.teal_200));

        user.setGender("woman");

    }
    public void testButtonGenderOther(View view){
        setButtonsGender();
        buttonGenderMan.setBackgroundColor( getResources().getColor(R.color.teal_200));
        buttonGenderWoman.setBackgroundColor( getResources().getColor(R.color.teal_200));
        buttonGenderOther.setBackgroundColor( getResources().getColor(R.color.teal_700));

        user.setGender("other");

    }

    public void setButtonShowMeMen(View view){
        setButtonsShowMe();
        buttonShowMeMen.setBackgroundColor(getResources().getColor(R.color.teal_700));
        buttonShowMeWomen.setBackgroundColor(getResources().getColor(R.color.teal_200));
        buttonShowMeEveryone.setBackgroundColor(getResources().getColor(R.color.teal_200));

        user.setShowMe("men");

    }
    public void setButtonShowMeWomen(View view){

        setButtonsShowMe();
        buttonShowMeMen.setBackgroundColor(getResources().getColor(R.color.teal_200));
        buttonShowMeWomen.setBackgroundColor(getResources().getColor(R.color.teal_700));
        buttonShowMeEveryone.setBackgroundColor(getResources().getColor(R.color.teal_200));

        user.setShowMe("women");
    }

    public void setButtonShowMeEveryone(View view){
        setButtonsShowMe();
        buttonShowMeMen.setBackgroundColor(getResources().getColor(R.color.teal_200));
        buttonShowMeWomen.setBackgroundColor(getResources().getColor(R.color.teal_200));
        buttonShowMeEveryone.setBackgroundColor(getResources().getColor(R.color.teal_700));

        user.setShowMe("everyone");
    }

    public void setButtonColor(View view){
        buttonAtColor = findViewById(R.id.buttonAtColor);

        if(colorIsClicked){
            colorIsClicked = false;
            buttonAtColor.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        else{
            colorIsClicked = true;
            buttonAtColor.setBackgroundColor(getResources().getColor(R.color.teal_700));
        }
    }
    public void setButtonTall(View view){
        buttonAtTall = findViewById(R.id.buttonAtTall);

        if(tallIsClicked){
            tallIsClicked = false;
            buttonAtTall.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        else{
            tallIsClicked = true;
            buttonAtTall.setBackgroundColor(getResources().getColor(R.color.teal_700));
        }
    }
    public void setButtonMoney(View view){
        buttonAtMoney = findViewById(R.id.buttonAtMoney);

        if(moneyIsClicked){
            moneyIsClicked = false;
            buttonAtMoney.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        else{
            moneyIsClicked = true;
            buttonAtMoney.setBackgroundColor(getResources().getColor(R.color.teal_700));
        }

    }
    public void setButtonHouse(View view){
        buttonAtHouse = findViewById(R.id.buttonAtHouse);

        if(houseIsClicked){
            houseIsClicked = false;
            buttonAtHouse.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        else{
            houseIsClicked = true;
            buttonAtHouse.setBackgroundColor(getResources().getColor(R.color.teal_700));
        }

    }
    public void setButtonDF(View view){
        buttonAtDF = findViewById(R.id.buttonAtDF);

        if(DFisClicked){
            DFisClicked = false;
            buttonAtDF.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        else {

            DFisClicked = true;
            buttonAtDF.setBackgroundColor(getResources().getColor(R.color.teal_700));
        }

    }

    public void saveAttributeData(View view){

        if(colorIsClicked) user.setRace(true);
        if(tallIsClicked) user.setTall(true);
        if(houseIsClicked) user.setHouse(true);
        if(moneyIsClicked) user.setMoney(true);
        if(DFisClicked) user.setDateFirst(true);

        user.setDidFirstSetUp(true);

        user.setAge(textAge);
        user.setName(textFirstName);

        user.updateGender();
        user.updateShowMe();
        user.updateName();
        user.updateAge();
        user.update();

        Intent intent = new Intent(InitialSetUp.this, MainActivity.class);
        startActivity(intent);
    }

    public void setNameOnFirebase(View view){

       editFirstName = findViewById(R.id.editFirstName);
       String textFirstName = editFirstName.getText().toString();
        //String textFirstName = "testeapenas";

        if(!textFirstName.isEmpty()){
            user.setName(textFirstName);
        }
    }

    public void setAgeOnFirebase(View view){

        editAge = findViewById(R.id.editAge);
        String textAge = editAge.getText().toString();

        Toast.makeText(this, textAge, Toast.LENGTH_SHORT).show();

        if(!textAge.isEmpty()){

            user.setAge(textAge);

        }
    }

    public void setButtonsGender(){

        buttonGenderMan = findViewById(R.id.buttonGenderMan);
        buttonGenderWoman = findViewById(R.id.buttonGenderWoman);
        buttonGenderOther = findViewById(R.id.buttonGenderOther);

    }

    public void setButtonsShowMe(){

        buttonShowMeMen = findViewById(R.id.buttonShowMeMen);
        buttonShowMeWomen = findViewById(R.id.buttonShowMeWomen);
        buttonShowMeEveryone = findViewById(R.id.buttonShowMeEveryone);

    }

    public void initialConfig(){

        editFirstName = findViewById(R.id.editFirstName);
        editAge = findViewById(R.id.editAge);

    }
}