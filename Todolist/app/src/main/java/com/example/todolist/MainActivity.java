package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.todolist.calendar.Calendar_Activity;
import com.example.todolist.R;

public class MainActivity extends AppCompatActivity {

    Calendar_Activity calendar_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar_activity = (Calendar_Activity)findViewById(R.id.calendar_view);
    }
}
