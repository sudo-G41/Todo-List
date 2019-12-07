package com.example.todolist.sharecalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.todolist.R;

public class ShareMainActivity extends AppCompatActivity {

    Sh_Calendar_Activity calendar_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_main);
        calendar_activity = (Sh_Calendar_Activity)findViewById(R.id.sh_calendar_view);
    }
}
