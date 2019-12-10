package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.sharecalendar.Sh_Calendar_Activity;
import com.example.todolist.sharecalendar.ShareButtenLocale;

public class ShareMainActivity extends AppCompatActivity {

    Sh_Calendar_Activity sh_calendar_activity;
    Intent intent;
    String str;

    public ShareMainActivity(){
    }

    public String getStr() {
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        str = intent.getStringExtra("code");
        setContentView(R.layout.activity_share_main);
        Log.e("ㅋㅋㅋㅋㅋㅋㅋ","ㅋㅋㅋㅋ");
        sh_calendar_activity = (Sh_Calendar_Activity)findViewById(R.id.sh_calendar_view);
    }
}
