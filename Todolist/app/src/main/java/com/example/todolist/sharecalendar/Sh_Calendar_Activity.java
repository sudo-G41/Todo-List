package com.example.todolist.sharecalendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.ShareMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Sh_Calendar_Activity extends LinearLayout {
    ImageButton NextBtn, PreviousBtn;
    Button CurrentDate;
    GridView gridView;
    private  static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.KOREAN);
    Context context;

    /*공유용*/
    String LoginCode ="";
    Button kr, jp;
    String LocalName = "kr";
    boolean Localbtn = true;
//    EditText Login;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    /********/

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.KOREAN);
    SimpleDateFormat monthFomat = new SimpleDateFormat("MMMM",Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

    AlertDialog alertDialog;
    AlertDialog timecalendar;
    Sh_MyGridAdapter myGridAdapter;

    ArrayList<Date> dates = new ArrayList<>();
    ArrayList<Sh_Events> eventsList = new ArrayList<>();
    int alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinuit;
    int y, m, d;
    long diff;

    public Sh_Calendar_Activity(Context context) {
        super(context);
    }

    public Sh_Calendar_Activity(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        IntializeLayout();
        SetUpCalendar();

        /*공유용*/
        kr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectKorea();
                SetUpCalendar();
            }
        });
        jp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectJapan();
                SetUpCalendar();
            }
        });
        /********************************************/

        CurrentDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog date = new DatePickerDialog(context, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SetUpCalendar();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                date.setMessage("이동할 날짜를 선택해 주세요");
                date.getDatePicker().setCalendarViewShown(false);
                date.getDatePicker().setSpinnersShown(true);
                date.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                date.show();
            }
        });

        PreviousBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });
        NextBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFomat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                DocumentReference ref = db.collection("share").document(LoginCode)
                        .collection(LocalName).document(year)
                        .collection(month).document(date);
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setCancelable(true);
                            if(doc.exists()){
                                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_show_events_layout, null);
                                RecyclerView recyclerView = showView.findViewById(R.id.sh_EventsRV);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setHasFixedSize(true);
                                ArrayList arr = (ArrayList) doc.get("event");
                                ArrayList<Sh_Events> array = new ArrayList<>();
                                array.addAll(arr);
                                Sh_EventRecyclerAdapter eventRecyclerAdapter = new Sh_EventRecyclerAdapter(showView.getContext(), array, LoginCode);//특정한 날자가 주어졌을때 그 날짜에 해당하는 배열 하나 반환
                                recyclerView.setAdapter(eventRecyclerAdapter);
                                eventRecyclerAdapter.notifyDataSetChanged();
                                builder.setView(showView);
                                alertDialog = builder.create();
                                alertDialog.show();
                                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        SetUpCalendar();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(context, "비어있습니다.", Toast.LENGTH_SHORT).show();
                                Log.e("그리드뷰 실패","에러다..");
                            }
                        }
                    }
                });
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_add_newevent_layout, null);
                final EditText EventName = addView.findViewById(R.id.sh_eventname);
                final TextView EventDate = addView.findViewById(R.id.sh_eventdays);
                final TextView EventTime = addView.findViewById(R.id.sh_eventtime);
                ImageButton SetTime = addView.findViewById(R.id.sh_seteventtime);
                final CheckBox alarmMe = addView.findViewById(R.id.sh_alarmme);
                final Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(dates.get(position));
                alarmYear = dateCalendar.get(Calendar.YEAR);
                alarmMonth = dateCalendar.get(Calendar.MONTH);
                alarmDay = dateCalendar.get(Calendar.DAY_OF_MONTH);

                Button AddEvent = addView.findViewById(R.id.sh_addevent);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder newBuilder = new AlertDialog.Builder(context);
                        newBuilder.setCancelable(true);
                        final View newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_calendar_layout, null);
                        Button cancel = newView.findViewById(R.id.eventcaldar_cancel);
                        Button promise = newView.findViewById(R.id.eventcaldar_promise);

                        cancel.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timecalendar.dismiss();
                            }
                        });

                        CalendarView calendarView = newView.findViewById(R.id.set_end_time);
                        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                                y = year;
                                m = month;
                                d = day;
                            }
                        });

                        promise.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.YEAR, y);
                                c.set(Calendar.MONTH, m);
                                c.set(Calendar.DAY_OF_MONTH, d);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREAN);
                                String str = format.format(c.getTime());
                                EventDate.setText(str);

                                diff = c.getTime().getTime()-dateCalendar.getTime().getTime();
                                Log.e("시간 차이", ""+diff);

                                timecalendar.dismiss();
                            }
                        });

                        newBuilder.setView(newView);
                        timecalendar = newBuilder.create();
                        timecalendar.show();
                    }
                });

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        diff = diff/(24*60*60*1000);
                        int i = 0;
                        while(i<(int)diff){
                            String da = eventDateFormat.format(dates.get(position+i));
                            String mo = monthFomat.format(dates.get(position+i));
                            String ye = yearFormat.format(dates.get(position+i));
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), da, mo, ye, position+1);
                            Log.e("savedate : ",da);
                            i++;
                        }
                        i += position;
                        String date = eventDateFormat.format(dates.get(i));
                        String month = monthFomat.format(dates.get(i));
                        String year = yearFormat.format(dates.get(i));
                        if(alarmMe.isChecked()){
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year, i);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinuit);
                        }
                        else{
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year, i);
                        }
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });
    }

    public Sh_Calendar_Activity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent(final String event, final String time, final String date, final String month, final String year, int p){
        final DocumentReference ref = db.collection("share").document(LoginCode)
                .collection(LocalName).document(year)
                .collection(month).document(date);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    Sh_Events ev = new Sh_Events(event, time, date, month, year);
                    if(doc.exists()){
                        ref.update("event", FieldValue.arrayUnion(ev));
                    }
                    else{
                        ArrayList<Sh_Events> arr = new ArrayList<>();
                        arr.add(ev);
                        Map<String, ArrayList> map = new HashMap<>();
                        map.put("event", arr);
                        ref.set(map);
                    }
                }
            }
        });
        if(Localbtn){
            SelectJapan();
            final String da = eventDateFormat.format(dates.get(p));
            final String mo = monthFomat.format(dates.get(p));
            final String ye = yearFormat.format(dates.get(p));
            final DocumentReference ref2 = db.collection("share").document(LoginCode)
                    .collection(LocalName).document(ye)
                    .collection(mo).document(da);
            ref2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        Sh_Events ev = new Sh_Events(event, time, da, mo, ye);
                        if(doc.exists()){
                            ref2.update("event", FieldValue.arrayUnion(ev));
                        }
                        else{
                            ArrayList<Sh_Events> arr = new ArrayList<>();
                            arr.add(ev);
                            Map<String, ArrayList> map = new HashMap<>();
                            map.put("event", arr);
                            ref2.set(map);
                        }
                    }
                }
            });
            SelectKorea();
        }
        else {
            SelectKorea();
            final String da = eventDateFormat.format(dates.get(p));
            final String mo = monthFomat.format(dates.get(p));
            final String ye = yearFormat.format(dates.get(p));
            final DocumentReference ref2 = db.collection("share").document(LoginCode)
                    .collection(LocalName).document(ye)
                    .collection(mo).document(da);
            ref2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        Sh_Events ev = new Sh_Events(event, time, da, mo, ye);
                        if(doc.exists()){
                            ref2.update("event", FieldValue.arrayUnion(ev));
                        }
                        else{
                            ArrayList<Sh_Events> arr = new ArrayList<>();
                            arr.add(ev);
                            Map<String, ArrayList> map = new HashMap<>();
                            map.put("event", arr);
                            ref2.set(map);
                        }
                    }
                }
            });
            SelectJapan();
        }
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void IntializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sh_calendar_layout, this);
        NextBtn = view.findViewById(R.id.sh_nextBtn);
        PreviousBtn = view.findViewById(R.id.sh_previousBtn);
        CurrentDate = view.findViewById(R.id.sh_currentDate);
        gridView = view.findViewById(R.id.sh_gridview);

        /*공유용*/
//        Login = (EditText)findViewById(R.id.login);
        this.LoginCode = "su";
        kr = view.findViewById(R.id.kr);
        jp = view.findViewById(R.id.ja);
        /****************************************/
    }

    private void SetUpCalendar(){
        db.collection("share").document(LoginCode)
                .collection(LocalName).document(yearFormat.format(calendar.getTime()))
                .collection(monthFomat.format(calendar.getTime()))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String currwntDate = dateFormat.format(calendar.getTime());
                            CurrentDate.setText(currwntDate);
                            dates.clear();
                            Calendar monthCalendar = (Calendar) calendar.clone();
                            monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
                            int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
                            monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
                            eventsList.clear();
                            ArrayList<Sh_Events> arr = new ArrayList<>();
                            int count = 0;
                            for(QueryDocumentSnapshot var : task.getResult()){
                                count++;
                                arr.addAll((ArrayList<Sh_Events>) var.get("event"));
                            }
                            for(Object var : arr) {
                                Map<String, String> map = (Map<String, String>) var;
                                Sh_Events eve = new Sh_Events(map.get("event"), map.get("time"), map.get("date"), map.get("month"), map.get("year"));
                                eventsList.add(eve);
                            }
                            Log.wtf("eventList 동작 횟수 : ", ""+count);
//                            CollectEventsPerMonth(monthFomat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));//eventsList 를 초기화 해주는 구문
                            while (dates.size() < MAX_CALENDAR_DAYS){
                                dates.add(monthCalendar.getTime());
                                monthCalendar.add(Calendar.DAY_OF_MONTH,1);
                            }
                            Log.e("그리드뷰 어뎁터 중간 보고 : ", "내용물 : "+eventsList.toString()+"\t클래스 : "+eventsList.getClass());
//                            Log.e("그리드뷰 어뎁터 중간 보고 : ", "0번 클래스 : "+eventsList);
                            myGridAdapter = new Sh_MyGridAdapter(context, dates, calendar, eventsList);
                            gridView.setAdapter(myGridAdapter);
                            Log.e("셋업캘린더","성공! (size "+eventsList.size()+")");
                        }
                        else{
                            Log.e("셋업켈린더 에러","응 실패야");
                        }
                    }
                });
    }
    public void SelectKorea(){
        this.LocalName = "kr";
        this.dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.KOREAN);
        this.monthFomat = new SimpleDateFormat("MMMM",Locale.KOREAN);
        this.yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
        this.eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
        this.Localbtn = true;
        Log.e("언어설정","한국어");
    }
    public void SelectJapan(){
        this.LocalName = "jp";
        this.dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.JAPAN);
        this.monthFomat = new SimpleDateFormat("MMMM",Locale.JAPAN);
        this.yearFormat = new SimpleDateFormat("yyyy", Locale.JAPAN);
        this.eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN);
        this.Localbtn = false;
        Log.e("언어설정","일본어");
    }
}
