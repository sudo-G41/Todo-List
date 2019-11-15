package com.example.todolist.ShareList;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShareCalendar_Activity extends LinearLayout {
    ImageButton NextBtn, PreviousBtn;
    TextView currentDate;
    GridView gridView;
    private  static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.KOREAN);
    Context context;

    List<Date> data = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.KOREAN);
    SimpleDateFormat monthFomat = new SimpleDateFormat("MMMM",Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);

    public ShareCalendar_Activity(Context context) {
        super(context);
    }

    public ShareCalendar_Activity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
}
/*https://www.youtube.com/watch?v=ubvACPf5_tQ */
/* ha*/