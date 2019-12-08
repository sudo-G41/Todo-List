package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.todolist.calendar.Calendar_Activity;
import com.example.todolist.R;
import com.example.todolist.sharecalendar.ShareButtenLocale;

public class MainActivity extends AppCompatActivity {

    Calendar_Activity calendar_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar_activity = (Calendar_Activity)findViewById(R.id.calendar_view);
        final Intent intent = new Intent(MainActivity.this, ShareButtenLocale.class);
        Button b = (Button)findViewById(R.id.shraecalendar) ;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
//                finish();
            }
        });
    }
}
