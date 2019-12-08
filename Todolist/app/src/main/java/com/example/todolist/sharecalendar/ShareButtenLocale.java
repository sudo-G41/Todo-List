package com.example.todolist.sharecalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.todolist.R;
import com.example.todolist.ShareMainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShareButtenLocale extends AppCompatActivity {
    Button SharIn, SignUp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_butten_locale);
        SharIn = (Button)findViewById(R.id.share_in);
        SignUp = (Button)findViewById(R.id.sign_up);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SharIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareButtenLocale.this, ShareMainActivity.class);
//                intent.putExtra()
                startActivity(intent);
                finish();
            }
        });
    }
}
