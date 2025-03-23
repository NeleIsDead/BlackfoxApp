package com.blackfox.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPlaceViewActivity extends AppCompatActivity {
    CalendarView calendarView;
    Calendar actualSelectedDate;
    TextView addressTextView, firstShiftWorkerNum, secondShiftWorkerNum;
    public int workersForFirstShift,workersForSecondShift;
    private final String LOG_TAG = "AdminPlaceViewActivity";
    Bundle bundle;
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

        bundle = getIntent().getExtras();
        assert bundle != null;
        calendarView = findViewById(R.id.calendarViewAdmin);
        addressTextView = findViewById(R.id.addressText);
        API api = RetrofitBuilder.api();
        actualSelectedDate = Calendar.getInstance();
        actualSelectedDate.setTimeInMillis(System.currentTimeMillis() + 86000000);
        firstShiftWorkerNum = findViewById(R.id.workersForFirstShift);
        secondShiftWorkerNum = findViewById(R.id.workersForSecondShift);
        addressTextView.setText(bundle.getString("address"));

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
    }
    public void roundMyTime(@NonNull Calendar actualSelectedDate){
        actualSelectedDate.setTimeInMillis(actualSelectedDate.getTimeInMillis());
        actualSelectedDate.set(Calendar.SECOND, 0);
        actualSelectedDate.set(Calendar.MINUTE, 0);
        actualSelectedDate.set(Calendar.MILLISECOND, 0);
        actualSelectedDate.set(Calendar.HOUR_OF_DAY, 0);
        Log.d(LOG_TAG, "After rounding " + actualSelectedDate.getTimeInMillis());
    }

}