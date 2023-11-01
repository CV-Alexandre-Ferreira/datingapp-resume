package com.example.datingappdev.model;

import android.location.Location;

import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.UserFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {


    private String id;
    private String name;
    private String email;
    private String password;
    private String age;
    private String gender;
    private String showMe;

    private double latitude;
    private double longitude;
    private int distanceOfInterest;
    private Boolean isHouse = false;
    private Boolean isTall = false;
    private Boolean isMoney= false;
    private Boolean isSexNow= false;
    private Boolean isDateFirst= false;
    private Boolean isRace= false;
    private Boolean didFirstSetUp = false;

    public User() {
    }


    public void saveUserOnDatabase(){
        DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference user = firebaseRef.child("users").child(getId());

        user.setValue(this);
    }

    public void update(){
        String identificadorUsuario = UserFirebase.getUserId();
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("users")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren(valoresUsuario);
    }

    public void updateLocation(){
        String identificadorUsuario = UserFirebase.getUserId();
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("users")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = mapLocationConverter();
        usuariosRef.updateChildren(valoresUsuario);
    }

    public void updateName(){
        String identificadorUsuario = UserFirebase.getUserId();
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("users")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = mapNameConverter();
        usuariosRef.updateChildren(valoresUsuario);
    }

    public void updateAge(){

        String identificadorUsuario = UserFirebase.getUserId();
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("users")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = mapAgeConverter();
        usuariosRef.updateChildren(valoresUsuario);

    }

    public void updateGender(){

        String identificadorUsuario = UserFirebase.getUserId();
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("users")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = mapGenderConverter();
        usuariosRef.updateChildren(valoresUsuario);

    }

    public void updateShowMe(){

        String identificadorUsuario = UserFirebase.getUserId();
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("users")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = mapShowMeConverter();
        usuariosRef.updateChildren(valoresUsuario);

    }

    public int getDistanceOfInterest() {
        return distanceOfInterest;
    }

    public void setDistanceOfInterest(int distanceOfInterest) {
        this.distanceOfInterest = distanceOfInterest;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShowMe() {
        return showMe;
    }

    public void setShowMe(String showMe) {
        this.showMe = showMe;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public Boolean getDidFirstSetUp() {
        return didFirstSetUp;
    }

    public void setDidFirstSetUp(Boolean didFirstSetUp) {
        this.didFirstSetUp = didFirstSetUp;
    }

    public Boolean getHouse() {
        return isHouse;
    }

    public void setHouse(Boolean house) {
        isHouse = house;
    }

    public Boolean getTall() {
        return isTall;
    }

    public void setTall(Boolean tall) {
        isTall = tall;
    }

    public Boolean getMoney() {
        return isMoney;
    }

    public void setMoney(Boolean money) {
        isMoney = money;
    }

    public Boolean getSexNow() {
        return isSexNow;
    }

    public void setSexNow(Boolean sexNow) {
        isSexNow = sexNow;
    }

    public Boolean getDateFirst() {
        return isDateFirst;
    }

    public void setDateFirst(Boolean dateFirst) {
        isDateFirst = dateFirst;
    }

    public Boolean getRace() {
        return isRace;
    }

    public void setRace(Boolean race) {
        isRace = race;
    }


    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();

        //usuarioMap.put("email", getEmail());
        //usuarioMap.put("name", getName());

        usuarioMap.put("house" , getHouse());
        usuarioMap.put("tall" , getTall());
        usuarioMap.put("money" , getMoney());
        usuarioMap.put("race" , getRace());
        usuarioMap.put("sexNow" , getSexNow());
        usuarioMap.put("dateFirst" , getDateFirst());
        usuarioMap.put("didFirstSetUp" , getDidFirstSetUp());
        usuarioMap.put("distanceOfInterest", getDistanceOfInterest());

        return usuarioMap;
    }

    public Map<String, Object> mapLocationConverter(){

        HashMap<String,Object> usuarioMap = new HashMap<>();

        usuarioMap.put("latitude", getLatitude());
        usuarioMap.put("longitude", getLongitude());

        return usuarioMap;

    }

    public Map<String, Object> mapNameConverter(){

        HashMap<String,Object> usuarioMap = new HashMap<>();

        usuarioMap.put("name", getName());

        return usuarioMap;

    }

    public Map<String, Object> mapAgeConverter(){

        HashMap<String,Object> usuarioMap = new HashMap<>();

        usuarioMap.put("age", getAge());

        return usuarioMap;

    }

    public Map<String, Object> mapGenderConverter(){

        HashMap<String,Object> usuarioMap = new HashMap<>();

        usuarioMap.put("gender", getGender());

        return usuarioMap;

    }

    public Map<String, Object> mapShowMeConverter(){

        HashMap<String,Object> usuarioMap = new HashMap<>();

        usuarioMap.put("showMe", getShowMe());

        return usuarioMap;

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String senha) {
        this.password = senha;
    }

}
