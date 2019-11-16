package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.todolist.ShareList.ShareCalendar_Activity;

public class MainActivity extends AppCompatActivity {

    ShareCalendar_Activity shareCalendar_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shareCalendar_activity = (ShareCalendar_Activity)findViewById(R.id.shareviwe);
    }
}
