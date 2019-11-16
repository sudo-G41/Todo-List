package com.example.todolist.ShareList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyGridAdapter extends ArrayAdapter {
    List<Date> dates;
    private final Calendar currentDate;
    Calendar CurrentDate;
    List<Events> events;
    LayoutInflater inflater;

    public MyGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, R.layout.single_cell_layout);
        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Date monthDate = dates.get(position);
        Calendar dateCalender = Calendar.getInstance();
        dateCalender.setTime(monthDate);
        int DayNo = dateCalender.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalender.get(Calendar.MONDAY)+1;
        int displayYear = dateCalender.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONDAY)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.green));
        }
        else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }

        TextView Day_Number = view.findViewById(R.id.calendar);
        Day_Number.setText(String.valueOf(DayNo));

        return view;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
