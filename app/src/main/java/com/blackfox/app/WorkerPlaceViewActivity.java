package com.blackfox.app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.Calendar;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerPlaceViewActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView addressTextView, firstShiftWorkerNum, secondShiftWorkerNum;
    CheckBox enrollFirstShift,enrollSecondShift;
    public int workersForFirstShift,workersForSecondShift;
    SharedPreferences sharedPreferences;
    String LOG_TAG = "WorkerPlaceViewActivity";
    String CODE_KEY_STRING = "savedUserDataString";
    Bundle bundle;
    Calendar actualSelectedDate;
    Button sendButton;
    
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

        bundle = getIntent().getExtras();
        assert bundle != null;
        addressTextView = findViewById(R.id.addressText);
        firstShiftWorkerNum = findViewById(R.id.workersForFirstShift);
        secondShiftWorkerNum = findViewById(R.id.workersForSecondShift);
        enrollFirstShift = findViewById(R.id.enrollFirstShift);
        enrollSecondShift = findViewById(R.id.enrollSecondShift);
        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);
        calendarView = findViewById(R.id.calendarView);
        addressTextView.setText(bundle.getString("address"));
        sendButton = findViewById(R.id.sendTimeButton);

        API api = RetrofitBuilder.api();

        actualSelectedDate = Calendar.getInstance();
        actualSelectedDate.setTimeInMillis(System.currentTimeMillis() + 86000000);
        Log.d(LOG_TAG, "Before rounding" + actualSelectedDate.getTimeInMillis());
        roundMyTime(actualSelectedDate);
        calendarView.setDate(actualSelectedDate.getTimeInMillis());
        Log.d(LOG_TAG, "After rounding " + actualSelectedDate.getTimeInMillis());
        Log.d(LOG_TAG, sharedPreferences.getString(CODE_KEY_STRING, ""));

        Call<Data> call = api.getWorkerNumForShift(new Count(actualSelectedDate.getTimeInMillis()/1000, addressTextView.getText().toString()));
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                workersForFirstShift = data.getFirst();
                workersForSecondShift = data.getSecond();
                firstShiftWorkerNum.setText("Уже на 1 смене: " + workersForFirstShift +" чел");
                secondShiftWorkerNum.setText("Уже на 2 смене: " + workersForSecondShift +" чел");
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d(LOG_TAG, "Something went wrong with the date :(");
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                actualSelectedDate.set(year, month, dayOfMonth);
                roundMyTime(actualSelectedDate);
                Call<Data> call = api.getWorkerNumForShift(new Count(actualSelectedDate.getTimeInMillis()/1000, addressTextView.getText().toString()));
                call.enqueue(new Callback<Data>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Data data = response.body();
                        workersForFirstShift = data.getFirst();
                        workersForSecondShift = data.getSecond();
                        Log.d(LOG_TAG, String.valueOf(workersForFirstShift));
                        Log.d(LOG_TAG, String.valueOf(workersForSecondShift));
                        String a = "Уже на 1 смене: " + workersForFirstShift + " чел";
                        String b = "Уже на 2 смене: " + workersForSecondShift + " чел";
                        firstShiftWorkerNum.setText(a);
                        secondShiftWorkerNum.setText(b);
                    }
                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        Log.d(LOG_TAG, "Something went wrong with the date :(");
                        Log.d(LOG_TAG, t.toString());
                    }
                });
            }
        });
        enrollFirstShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Текущая дата: " + actualSelectedDate.getTimeInMillis());
                Log.d(LOG_TAG, "Checked Status: " + enrollFirstShift.isChecked());
                if (workersForFirstShift > 6){
                    Snackbar.make(v, "На смене уже максимум работников, или произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                    enrollFirstShift.toggle();
                }
            }
        });
        enrollSecondShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Текущая дата: " + actualSelectedDate.getTimeInMillis());
                Log.d(LOG_TAG, "Checked Status: " + enrollSecondShift.isChecked());

                if (workersForSecondShift > 6){
                    Snackbar.make(v, "На смене уже максимум работников, или произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                    enrollSecondShift.toggle();
                }
            }
        });

        /*Button that sends off the request to enroll for a shift*/
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<String> call = api.enrollForShift(generateTime(actualSelectedDate.getTimeInMillis(), enrollFirstShift.isChecked(),enrollSecondShift.isChecked()));
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(LOG_TAG, "Sent reserved time " + generateTime(actualSelectedDate.getTimeInMillis(), enrollFirstShift.isChecked(),enrollSecondShift.isChecked()).getDate() + " to server");
                        Log.d(LOG_TAG, "First reserved : " + generateTime(actualSelectedDate.getTimeInMillis(), enrollFirstShift.isChecked(),enrollSecondShift.isChecked()).isFirst());
                        Log.d(LOG_TAG, "Second reserved : " + generateTime(actualSelectedDate.getTimeInMillis(), enrollFirstShift.isChecked(),enrollSecondShift.isChecked()).isSecond());
                        Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Snackbar.make(v, "Произошла ошибка :(", BaseTransientBottomBar.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, t.toString());
                    }
                });
            }
        });
    }

    /* Formatting and rounding Time down to 00:00:00 so the server can handle it */
    public void roundMyTime(@NonNull Calendar actualSelectedDate){
        actualSelectedDate.setTimeInMillis(actualSelectedDate.getTimeInMillis());
        actualSelectedDate.set(Calendar.SECOND, 0);
        actualSelectedDate.set(Calendar.MINUTE, 0);
        actualSelectedDate.set(Calendar.MILLISECOND, 0);
        actualSelectedDate.set(Calendar.HOUR_OF_DAY, 0);
        Log.d(LOG_TAG, "After rounding " + actualSelectedDate.getTimeInMillis());
    }

    /*
    Generates time object with user code, address of picked job place, booleans for shifts chosen
    and the actual UNIX TIME converting it to seconds instead of Millis because python is fucking weird
    */
    public Time generateTime(long pickedDate, boolean first, boolean second){
        return new Time(sharedPreferences.getString(CODE_KEY_STRING, ""),
                                                    bundle.getString("address"),
                                                    pickedDate/1000,
                                                    first,
                                                    second);
    }

}