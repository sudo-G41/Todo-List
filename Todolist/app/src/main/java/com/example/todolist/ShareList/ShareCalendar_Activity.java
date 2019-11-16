package com.example.todolist.ShareList;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.todolist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class ShareCalendar_Activity extends LinearLayout {
    ImageButton NextBtn, PreviousBtn;
    TextView CurrentDate;
    GridView gridView;
    private  static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.KOREAN);
    Context context;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.KOREAN);
    SimpleDateFormat monthFomat = new SimpleDateFormat("MMMM",Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);

    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;

    List<Date> data = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    DBOpenHelper dbOpenHelper;

    public ShareCalendar_Activity(Context context) {
        super(context);
    }

    public ShareCalendar_Activity(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        IntializeLayout();
        SetUpCalendar();

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                final View addView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.add_newevent_layout, null);
                final EditText EventName = addView.findViewById(R.id.events_id);
                final TextView EventTime = addView.findViewById(R.id.eventtime);
                ImageButton SetTime = addView.findViewById(R.id.seteventtime);
                Button AddEvent = addView.findViewById(R.id.addevent);
                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minuts = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, i);
                                c.set(Calendar.MINUTE, i1);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformate = new SimpleDateFormat("K:mm a", Locale.KOREAN);
                                String event_Time = hformate.format(c.getTime());
                                EventTime.setText(event_Time);
                            }
                        }, hours, minuts, false);
                        timePickerDialog.show();
                    }
                });
                final String date = dateFormat.format(data.get(i));
                final String month = monthFomat.format(data.get(i));
                final String year = yearFormat.format(data.get(i));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year);
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public ShareCalendar_Activity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEvent(String event, String time, String date, String month, String year){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
        Toast.makeText(context, "Event Saved", Toast.LENGTH_SHORT).show();;
    }

    private void IntializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sharecalendar_layout,this);
        NextBtn = view.findViewById(R.id.nextBtn);
        PreviousBtn = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.gridview);
    }
    private  void SetUpCalendar(){
        String currwntDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currwntDate);
        data.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_MONTH);
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);

        while (data.size() < MAX_CALENDAR_DAYS){
            data.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        myGridAdapter = new MyGridAdapter(context, data, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
    }
}
/*https://www.youtube.com/watch?v=ubvACPf5_tQ */