package com.blackfox.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class WorkerPlaceViewActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView addressTextView, firstShiftWorkerNum, secondShiftWorkerNum;
    CheckBox enrollFirstShift,enrollSecondShift;

    public int workersForFirstShift,workersForSecondShift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_worker_place_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        addressTextView = findViewById(R.id.addressText);
        firstShiftWorkerNum = findViewById(R.id.workersForFirstShift);
        secondShiftWorkerNum = findViewById(R.id.workersForSecondShift);
        enrollFirstShift = findViewById(R.id.enrollFirstShift);
        enrollSecondShift = findViewById(R.id.enrollSecondShift);

        addressTextView.setText(bundle.getString("address"));
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("calendar", year + " " + month + " " + dayOfMonth);
                workersForFirstShift = 0;
                workersForSecondShift = 6;
                firstShiftWorkerNum.setText("Уже на 1 смене: " + String.valueOf(workersForFirstShift) +" чел");
                secondShiftWorkerNum. setText("Уже на 2 смене: " + String.valueOf(workersForSecondShift) +" чел");
            }
        });
        enrollFirstShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workersForFirstShift < 6) {
                    //Отправить серверу инфу о зарезервированном времени
                    Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(v, "На смене уже максимум работников, или произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                    enrollFirstShift.toggle();
                }

            }

        });
        enrollSecondShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workersForSecondShift < 6) {
                    //Отправить серверу инфу о зарезервированном времени
                    Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(v, "На смене уже максимум работников, или произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                    enrollSecondShift.toggle();
                }

            }
        });
    }

}