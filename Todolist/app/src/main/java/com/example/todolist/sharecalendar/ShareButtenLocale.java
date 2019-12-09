package com.example.todolist.sharecalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.ShareMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShareButtenLocale extends AppCompatActivity {
    String LoginCode;
    Button SharIn, SignUp;
    Button butt;
    EditText code, login;
    TextView t;
    boolean q = true;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_butten_locale);
        login = (EditText)findViewById(R.id.login);
        SharIn = (Button)findViewById(R.id.share_in);
        SignUp = (Button)findViewById(R.id.sign_up);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.setVisibility(View.INVISIBLE);
                SharIn.setText("확인");
                q = false;
            }
        });

        SharIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(q) {
                    Intent intent = new Intent(ShareButtenLocale.this, ShareMainActivity.class);
                    String asd = login.getText().toString();
                    Log.e("LoginCode.", asd);
                    intent.putExtra("LoginCode", asd);
                    startActivity(intent);
                }
                else {
                    final String asd = login.getText().toString();
                    DocumentReference ref = db.collection("share").document(asd);
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(ShareButtenLocale.this, "이미 있는 코드입니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ShareButtenLocale.this, "생성되었습니다..", Toast.LENGTH_SHORT).show();
                                    LoginCode = asd;
                                    Intent intent = new Intent(ShareButtenLocale.this, ShareMainActivity.class);
                                    intent.putExtra("LoginCode", asd);
                                    startActivity(intent);
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }
}
