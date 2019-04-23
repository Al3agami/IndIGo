package com.agamidev.indigo.Activitites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.agamidev.indigo.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    public void fullEarth(View v){
        Intent mainIntent = new Intent(HomeActivity.this,MainActivity.class);
        HomeActivity.this.startActivity(mainIntent);
    }
}
