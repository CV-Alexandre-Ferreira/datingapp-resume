package com.example.datingappdev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.config.Permissions;
import com.example.datingappdev.fragment.FeedFragment;
import com.example.datingappdev.fragment.FeedFragmentTestCard;
import com.example.datingappdev.fragment.MatchesFragment;
import com.example.datingappdev.fragment.UserInfoFragment;
import com.example.datingappdev.helper.Base64Custom;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.LatLng;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Toolbar toolbar;
    private String[] permissoesNecessarias = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseConfig.getFirebaseAuthentication();

        Permissions.validarPermissoes(permissoesNecessarias, this, 1);

        getUserLocation();

        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Feed");
        setSupportActionBar(toolbar);


        //Setting tabs
/*
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Feed", FeedFragment.class)
                        .add("Matches", MatchesFragment.class)
                        .create()
        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

 */

        //Configuracao bottom navigation view

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        habilitarNavegacao(bottomNavigationViewEx);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
        fragmentTransaction.replace(R.id.viewPager, new FeedFragmentTestCard()).commit();


    }

    public void getUserLocation(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                Double latitude = location.getLatitude();
                Log.d("localizacao", "localizacao: " + location.toString());

                User user = new User();


                String email = auth.getCurrentUser().getEmail();
                user.setEmail(email);
                String idUser = Base64Custom.encodeBase64(user.getEmail());
                user.setId(idUser);

                user.setLatitude(location.getLatitude());
                user.setLongitude(location.getLongitude());

                user.updateLocation();

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1000,
                    locationListener

            );
        }
        else{
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    1000,
                    locationListener );

        }
    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx){
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){

                    case R.id.ic_home:
                        //fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
                        fragmentTransaction.replace(R.id.viewPager, new FeedFragmentTestCard()).commit();
                        toolbar.setTitle("Feed");
                        return true;

                    case R.id.ic_pesquisa:
                        fragmentTransaction.replace(R.id.viewPager, new MatchesFragment()).commit();
                        toolbar.setTitle("Chats");
                        return true;

                    case R.id.userInfo:
                        fragmentTransaction.replace(R.id.viewPager, new UserInfoFragment()).commit();
                        toolbar.setTitle("You");
                        return true;

                }

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuQuit:

                locationManager.removeUpdates(locationListener);
                signOut();
                finish();
                Intent intent = new Intent(MainActivity.this, InitialActivity.class);
                startActivity(intent);
                break;

            case R.id.cardMenu:
                Intent intent1 = new Intent(MainActivity.this, CardActivity.class);
                startActivity(intent1);
                break;



        }

        return super.onOptionsItemSelected(item);
    }

    public void signOut(){

        try{

            auth.signOut();

        }catch (Exception e){e.printStackTrace();}

    }

    public void openInitialLogoScreen(){
        Intent intent = new Intent(MainActivity.this, InitialActivity.class);
        startActivity(intent);
    }

}