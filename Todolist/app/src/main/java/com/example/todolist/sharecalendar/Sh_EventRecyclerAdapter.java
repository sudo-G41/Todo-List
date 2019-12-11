package com.example.todolist.sharecalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Sh_EventRecyclerAdapter extends RecyclerView.Adapter<Sh_EventRecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Sh_Events> arrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String loginCode;

    public Sh_EventRecyclerAdapter(Context context, ArrayList<Sh_Events> arrayList, String loginCode) {
        this.context = context;
        this.arrayList = arrayList;
        this.loginCode =loginCode;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_show_events_rowlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Map<String, String> map = (Map<String, String>) arrayList.get(position);;
        final Sh_Events events = new Sh_Events(map.get("event"), map.get("time"), map.get("date"), map.get("month"), map.get("year"));
        holder.Event.setText(events.getEVENT());
        holder.DateTxt.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCalendarEvent(events);
                arrayList.remove(position);
                notifyDataSetChanged();

                Toast.makeText(context, "Delete Event", Toast.LENGTH_SHORT).show();
            }
        });

        Calendar datecalendar = Calendar.getInstance();
        datecalendar.setTime(ConvertStringToDate(events.getDATE()));

        Calendar timecalendar = Calendar.getInstance();
        timecalendar.setTime(ConvertStringToTime(events.getTIME()));

        holder.setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.setAlarm.setImageResource(R.drawable.ic_action_alaram_off);
                updateEvent(events.getDATE(), events.getEVENT(), events.getTIME(), "off");
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView DateTxt, Event, Time;
        ImageButton delete;
        ImageButton setAlarm;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DateTxt = itemView.findViewById(R.id.sh_eventdate);
            Event = itemView.findViewById(R.id.sh_eventname);
            Time = itemView.findViewById(R.id.sh_eventime);
            delete = itemView.findViewById(R.id.sh_delete);
            setAlarm = itemView.findViewById(R.id.sh_alarameBtn);
        }
    }

    private Date ConvertStringToDate(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
        Date date = null;
        try{
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;
    }

    private Date  ConvertStringToTime(String eventDate){
        SimpleDateFormat format = new SimpleDateFormat("kk:mm", Locale.KOREAN);
        Date date = null;
        try{
            date = format.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;
    }

    private void deleteCalendarEvent(Sh_Events del){
        int i = del.MONTH.indexOf("월");
        String lo;
        String lo2;
        String month;
        if(i>0){
            lo = "kr";
            lo2 = "jp";
            month = del.MONTH.substring(0, del.MONTH.length()-1)+"月";

        }
        else{
            lo = "jp";
            lo2 = "kr";
            month = del.MONTH.substring(0, del.MONTH.length()-1)+"월";
        }
        DocumentReference ref = db.collection("share").document(loginCode)
                .collection(lo).document(del.YEAR)
                .collection(del.MONTH).document(del.DATE);
        ref.update("event", FieldValue.arrayRemove(del));
        ref = db.collection("share").document(loginCode)
                .collection(lo2).document(del.YEAR)
                .collection(month).document(del.DATE);
        ref.update("event", FieldValue.arrayRemove(del));
    }

    private  void updateEvent(String date, String event, String time, String notify){
//        dbOpenHelper = new FBOpenHelper();
//        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
//        dbOpenHelper.updateEvent(date, event, time, notify, database);
//        dbOpenHelper.close();
    }
}
