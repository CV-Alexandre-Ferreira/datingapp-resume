package com.example.datingappdev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MatchActivity extends AppCompatActivity {
    private Button buttonKeepSwiping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        buttonKeepSwiping = findViewById(R.id.keepSwipingButton);
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple_200));
        toolbar.setTitle("");
        setSupportActionBar( toolbar );

        buttonKeepSwiping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}