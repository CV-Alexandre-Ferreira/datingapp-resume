package com.example.datingappdev.model;

import com.example.datingappdev.config.FirebaseConfig;
import com.google.firebase.database.DatabaseReference;

public class Chat {

    private String idSender;
    private String idReceiver;
    private String lastMessage;
    private User showingUser;


    public void save(){
        DatabaseReference database = FirebaseConfig.getFirebaseDatabase();
        DatabaseReference chatRef = database.child("chats");
        chatRef.child(this.getIdSender()).child(this.getIdReceiver())
                .setValue(this);
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public User getShowingUser() {
        return showingUser;
    }

    public void setShowingUser(User showingUser) {
        this.showingUser = showingUser;
    }

}
