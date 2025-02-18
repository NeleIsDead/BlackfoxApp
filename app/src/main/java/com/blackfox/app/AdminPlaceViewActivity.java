package com.blackfox.app;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class AdminPlaceViewActivity extends AppCompatActivity {
    CalendarView calendarView;
    private final String LOG_TAG = "AdminPlaceViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_place_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        calendarView = findViewById(R.id.calendarViewAdmin);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar actuallySelectedDay = Calendar.getInstance();
                actuallySelectedDay.set(year, month, dayOfMonth);
                Log.d(LOG_TAG, String.valueOf(actuallySelectedDay.getTimeInMillis()));
            }
        });
    }

}