package com.blackfox.app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerPlaceViewActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView addressTextView, firstShiftWorkerNum, secondShiftWorkerNum;
    CheckBox enrollFirstShift,enrollSecondShift;
    public int workersForFirstShift,workersForSecondShift;
    SharedPreferences sharedPreferences;
    String LOG_TAG = "WorkerPlaceViewActivity";
    String CODE_KEY_STRING = "savedUserDataString";
    Bundle bundle;



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

        addressTextView.setText(bundle.getString("address"));


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_address))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);

        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);

        long pickedDate;
        calendarView = findViewById(R.id.calendarView);
        pickedDate = ((System.currentTimeMillis()/86400000) * 86400 + 86400);
        calendarView.setDate(pickedDate*1000);

        Log.d(LOG_TAG, String.valueOf(pickedDate));
        Call<Data> call = api.getWorkerNumForShift(new Count(pickedDate, addressTextView.getText().toString()));
        call.enqueue(new Callback<Data>() {
            @SuppressLint("SetTextI18n")
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
                long pickedDate = (calendarView.getDate()/86400000) * 86400;
                Log.d(LOG_TAG, String.valueOf(pickedDate));
                Call<Data> call = api.getWorkerNumForShift(new Count(pickedDate, addressTextView.getText().toString()));
                call.enqueue(new Callback<Data>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Data data = response.body();
                        workersForFirstShift = data.getFirst();
                        workersForSecondShift = data.getSecond();
                        Log.d(LOG_TAG, String.valueOf(workersForFirstShift));
                        Log.d(LOG_TAG, String.valueOf(workersForSecondShift));
                        firstShiftWorkerNum.setText("Уже на 1 смене: " + workersForFirstShift +" чел");
                        secondShiftWorkerNum.setText("Уже на 2 смене: " + workersForSecondShift +" чел");
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
                if (workersForFirstShift < 6) {
                    Call<String> call = api.enrollForShift(generateTime(pickedDate));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(LOG_TAG, String.valueOf(generateTime(pickedDate).getDate()));
                            Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Snackbar.make(v, "Произошла ошибка :(", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Snackbar.make(v, "На смене уже максимум работников, или произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                    enrollFirstShift.setActivated(false);
                }
            }
        });
        enrollSecondShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workersForSecondShift < 6) {
                    Call<String> call = api.enrollForShift(generateTime(pickedDate));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(LOG_TAG, String.valueOf(generateTime(pickedDate).getDate()));
                            Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Snackbar.make(v, "Произошла ошибка :(", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    });
                    Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(v, "На смене уже максимум работников, или произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                    enrollSecondShift.setActivated(false);
                }

            }
        });
    }
    public Time generateTime(long pickedDate){
        return new Time(sharedPreferences.getString(CODE_KEY_STRING, ""),
                                                    bundle.getString("address"),
                                                    pickedDate,
                                                    enrollFirstShift.isActivated(),
                                                    enrollSecondShift.isActivated());
    }

}