package com.example.todolist.calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Cipher;

import com.example.todolist.sharecalendar.ShareButtenLocale;

public class Calendar_Activity extends LinearLayout {
    ImageButton NextBtn, PreviousBtn;
    Button shraecalendar;
    Button CurrentDate;
    GridView gridView;
    private  static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.KOREAN);
    Context context;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMMM", Locale.KOREAN);
    SimpleDateFormat monthFomat = new SimpleDateFormat("MMMM",Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

    AlertDialog alertDialog;
    AlertDialog timecalendar;
    MyGridAdapter myGridAdapter;

    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    int alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinuit;
    int y, m, d;
    long diff;

    DBOpenHelper dbOpenHelper;

    public Calendar_Activity(Context context) {
        super(context);
    }

    public Calendar_Activity(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        IntializeLayout();
        SetUpCalendar();

        CurrentDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dates.get(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext(), CollectEventByDate(date));
                Log.e("date",date);
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
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText EventName = addView.findViewById(R.id.eventname);
                final TextView EventDate = addView.findViewById(R.id.eventdays);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                ImageButton SetTime = addView.findViewById(R.id.seteventtime);
                final CheckBox alarmMe = addView.findViewById(R.id.alarmme);
                final Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(dates.get(position));
                alarmYear = dateCalendar.get(Calendar.YEAR);
                alarmMonth = dateCalendar.get(Calendar.MONTH);
                alarmDay = dateCalendar.get(Calendar.DAY_OF_MONTH);

                Button AddEvent = addView.findViewById(R.id.addevent);
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
                        /*============================================*/
//                        Calendar calendar = Calendar.getInstance();
//                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//                        int minuts = calendar.get(Calendar.MINUTE);
//                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog
//                                , new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int hour, int minut) {
//                                Calendar c = Calendar.getInstance();
//                                c.set(Calendar.HOUR_OF_DAY, hour);
//                                c.set(Calendar.MINUTE, minut);
//                                c.setTimeZone(TimeZone.getDefault());
//                                SimpleDateFormat hformat = new SimpleDateFormat("k:mm a",Locale.KOREAN);
//                                String event_Time = hformat.format(c.getTime());
//                                EventTime.setText(event_Time);
//                                alarmHour = c.get(Calendar.HOUR_OF_DAY);
//                                alarmMinuit = c.get(Calendar.MINUTE);
//                            }
//                        }, hours, minuts, false);
//                        timePickerDialog.show();
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
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), da, mo, ye, "off");
                            i++;
                        }
                        i += position;
                        String date = eventDateFormat.format(dates.get(i));
                        String month = monthFomat.format(dates.get(i));
                        String year = yearFormat.format(dates.get(i));
                        if(alarmMe.isChecked()){
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year, "on");
                            SetUpCalendar();
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(alarmYear, alarmMonth, alarmDay, alarmHour, alarmMinuit);
                            setAlern(calendar, EventName.getText().toString(), EventTime.getText().toString(), getRequestCode(date, EventName.getText().toString(), EventTime.getText().toString()));
                        }
                        else{
                            SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year, "off");
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

    private int getRequestCode(String date, String event, String time){
        int code = 0;
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadIDEvents(date, event, time,database);
        while (cursor.moveToNext()){
            code = cursor.getInt(cursor.getColumnIndex(DBStructure.ID));
        }
        cursor.close();
        dbOpenHelper.close();

        return code;
    }

    private void setAlern(Calendar calendar, String event, String time, int RequestCode){
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("event", event);
        intent.putExtra("time", time);
        intent.putExtra("id", RequestCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RequestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private ArrayList<Events> CollectEventByDate(String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String Data = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event, time, Data, month, Year);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    public Calendar_Activity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent(String event, String time, String date, String month, String year, String notify){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, notify, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();
    }

    private void IntializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        shraecalendar= view.findViewById(R.id.shraecalendar);
        NextBtn = view.findViewById(R.id.nextBtn);
        PreviousBtn = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridview);
    }

    private void SetUpCalendar(){
        String currwntDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFomat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }
        Log.wtf("일반 캘린더 eventList Size : ",""+eventsList.size());
        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
    }
    private void CollectEventsPerMonth(String Month, String Year){
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month, Year, database);
        while(cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String data = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event, time, data, month, year);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }
}
