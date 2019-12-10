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
import java.util.HashMap;
import java.util.Map;

public class ShareButtenLocale extends AppCompatActivity {
    String strCode, strPassword;
    Button SharIn, SignUp;
    EditText code, password;
    Intent intent;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_butten_locale);
        code = (EditText)findViewById(R.id.sing_id);
        password = (EditText)findViewById(R.id.sing_password);
        SharIn = (Button)findViewById(R.id.share_in);
        SignUp = (Button)findViewById(R.id.sign_up);

        /*로그인버튼*/
        SharIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strCode = code.getText().toString();
                strPassword = password.getText().toString();
                DocumentReference ref = db.collection("share").document(strCode);
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(document.getString("Password").equals(strPassword)){
                                    intent = new Intent(ShareButtenLocale.this, ShareMainActivity.class);
                                    intent.putExtra("code",strCode);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(ShareButtenLocale.this, "잘못된 입력입니다.", Toast.LENGTH_SHORT).show();//비밀번호가 틀린 경우
                                    Log.e("비밀번호 틀림","저장된 비번:"+document.getString("Password")+"입력된 비번:"+strPassword);
                                }
                            } else {
                                Toast.makeText(ShareButtenLocale.this, "잘못된 입력입니다.", Toast.LENGTH_SHORT).show();//아이디가 없는 경우
                                Log.e("아이디 틀림","ㅇㅇ");
                            }
                        } else {
                            Log.d("TAG 로그인 화면", "get failed with : ", task.getException());
                        }
                    }
                });
            }
        });
        /****************************************************/

        /*Code 생성*/
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(ShareButtenLocale.this);
                builder.setCancelable(false);
                View newView = (View)getLayoutInflater().inflate(R.layout.sign_up,null);
                Button add, cancel;
                final EditText newCode, newPassword, Phone;
                newCode = newView.findViewById(R.id.newcode);
                newPassword = newView.findViewById(R.id.newpassword);
                Phone = newView.findViewById(R.id.newphone);
                add = newView.findViewById(R.id.addbtn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = newCode.getText().toString();
                        DocumentReference ref = db.collection("share").document(id);
                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Toast.makeText(ShareButtenLocale.this, "This Code is already in use.", Toast.LENGTH_SHORT).show();//아이디가 있는 경우
                                    } else {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("Password", newPassword.getText().toString());
                                        map.put("Phone", Phone.getText().toString());
                                        Toast.makeText(ShareButtenLocale.this, "Create Code!", Toast.LENGTH_SHORT).show();//아이디가 없는 경우
                                        alertDialog.dismiss();
                                    }
                                } else {
                                    Log.d("TAG 코드 생성", "get failed with : ", task.getException());
                                }
                            }
                        });
                    }
                });

                cancel = newView.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                builder.setView(newView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
