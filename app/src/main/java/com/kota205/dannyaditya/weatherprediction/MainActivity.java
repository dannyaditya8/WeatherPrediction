package com.kota205.dannyaditya.weatherprediction;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView mTime;
    TextView mTimezone;
    TextView mTemperature;
    TextView mHumidity;
    TextView mDisplayHumidity;
    TextView mDisplayRainSnow;
    TextView mRainStatus;
    TextView mStatus;
    TextView mTemplateAt;
    TextView mTemplateItWillBe;

    ImageView mImage;
    APIInterface apiInterface;
    String convertDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.container);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getData();
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    private void getData() {

        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);

        if (gpsTracker.canGetLocation()) {

            apiInterface = APIClient.getClient().create(APIInterface.class);
            Call<Weather> call = apiInterface.getInfo(gpsTracker.getLatitude() + "," + gpsTracker.getLongitude());

            call.enqueue(new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {


                    String clock = new SimpleDateFormat("hh:mm a").format(response.body().getCurrently().getTime());

                    mTime = findViewById(R.id.time);
                    mTimezone = findViewById(R.id.timezone);
                    mTemperature = findViewById(R.id.degree);
                    mHumidity = findViewById(R.id.humidityStatus);
                    mImage = findViewById(R.id.weatherIcon);
                    mStatus = findViewById(R.id.status);
                    mDisplayHumidity = findViewById(R.id.humidity);
                    mDisplayRainSnow = findViewById(R.id.rainsnow);
                    mRainStatus = findViewById(R.id.rainStatus);
                    mTemplateAt = findViewById(R.id.at);
                    mTemplateItWillBe = findViewById(R.id.willBe);


                    convertDegree = response.body().getCurrently().getTemperature().intValue() + "\u00B0";

                    mTimezone.setText(response.body().getTimezone());
                    mTemplateAt.setText("At" + " ");
                    mTemplateItWillBe.setText(" " + "it will be");
                    mTime.setText(clock);
                    mTemperature.setText(convertDegree);
                    mDisplayHumidity.setText("HUMIDITY");
                    mDisplayRainSnow.setText("Rain/Snow");
                    mHumidity.setText(String.valueOf(response.body().getCurrently().getHumidity()));
                    mRainStatus.setText("0 %");
                    mStatus.setText(response.body().getCurrently().getSummary());

                    if (response.body().getCurrently().getSummary().equals("Partly Cloudy")
                            || response.body().getCurrently().getSummary().equals("Mostly Cloudy")) {
                        mImage.setImageResource(R.drawable.ic_iconfinder_weather_2_1322065);
                    }


                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Please report the error !", Toast.LENGTH_LONG).show();
                }
            });


        }

    }



}
