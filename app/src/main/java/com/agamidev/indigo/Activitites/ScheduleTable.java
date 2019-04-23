package com.agamidev.indigo.Activitites;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.agamidev.indigo.R;

import java.util.ArrayList;

public class ScheduleTable extends AppCompatActivity {

    ImageView tableImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);

        tableImage = (ImageView) findViewById(R.id.tableimage);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Integer> images = (ArrayList<Integer>) bundle.get("image");
        int position = (Integer) bundle.get("index");
        tableImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),images.get(position)));
    }
}
