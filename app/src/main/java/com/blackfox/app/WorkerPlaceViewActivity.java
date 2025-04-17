package com.blackfox.app;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
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
    TextView addressTextView;
    EditText shiftStart, shiftEnd;
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

        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);
        calendarView = findViewById(R.id.calendarView);
        addressTextView.setText(bundle.getString("address"));
        sendButton = findViewById(R.id.sendTimeButton);
        shiftStart = findViewById(R.id.shiftStart);
        shiftEnd = findViewById(R.id.shiftEnd);

        API api = RetrofitBuilder.api();

        actualSelectedDate = Calendar.getInstance();

        actualSelectedDate.setTimeInMillis(System.currentTimeMillis() + 86000000);
        Log.d(LOG_TAG, "Before rounding" + actualSelectedDate.getTimeInMillis());

        roundMyTime(actualSelectedDate);
        calendarView.setDate(actualSelectedDate.getTimeInMillis());

        Log.d(LOG_TAG, "After rounding " + actualSelectedDate.getTimeInMillis());
        Log.d(LOG_TAG, sharedPreferences.getString(CODE_KEY_STRING, ""));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                actualSelectedDate.set(year, month, dayOfMonth);
                roundMyTime(actualSelectedDate);

            }
        });

        /*Button that sends off the request to enroll for a shift*/
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shiftStartButInString = shiftStart.getText().toString();
                String shiftEndButInString = shiftEnd.getText().toString();
                if (shiftEndButInString.matches("^(0?[0-9]|1[0-9]|2[0-3]):00|30$") && shiftStartButInString.matches("^(0?[0-9]|1[0-9]|2[0-3]):00|30$")){
                    Call<String> call = api.enrollForShift(generateTime(actualSelectedDate.getTimeInMillis(), shiftStartButInString,  shiftEndButInString));

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.d(LOG_TAG, "sent Time: " + shiftStartButInString +  shiftEndButInString);
                            Snackbar.make(v, "Время работы выбрано", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Snackbar.make(v, "Произошла ошибка :(", BaseTransientBottomBar.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, t.toString());
                        }
                    });
                }else Snackbar.make(v, "Введенное вами время записано в неверном формате верный формат: ЧЧ:MM", BaseTransientBottomBar.LENGTH_SHORT).show();


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
    and the actual UNIX TIME converting it to seconds instead of Millis
    */
    public Time generateTime(long pickedDate, String shiftStart, String shiftEnd){
        return new Time(sharedPreferences.getString(CODE_KEY_STRING, ""),
                                                    bundle.getString("address"),
                                                    pickedDate/1000,
                                                    stringTimeToFloatTime(shiftStart),
                                                    stringTimeToFloatTime(shiftEnd));
    }

    public float stringTimeToFloatTime(String time){
        String[] parts = time.split(":");
        float hrs = Float.parseFloat(parts[0]);
        float minutes = Float.parseFloat(parts[1]);
        return hrs + minutes/60;
    }

}