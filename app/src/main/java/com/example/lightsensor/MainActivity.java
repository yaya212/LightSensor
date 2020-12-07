package com.example.lightsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightListener;
    float maxvalue;
    ImageView imgView;
    TextView t;
    Animation topAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        imgView = (ImageView)findViewById(R.id.img);
        t = (TextView)findViewById(R.id.txtView);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        topAnim.setDuration(1500);




        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
        {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        }
        else
        {
            Toast.makeText(this, "The device has no Light Sensor", Toast.LENGTH_SHORT).show();
            finish();
        }
        maxvalue = lightSensor.getMaximumRange();


        lightListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_LIGHT)
                {

                    if(event.values[0] >= 0 && event.values[0] < (maxvalue/6))
                    {
                        imgView.setImageResource(R.drawable.sky_night);
                        t.setText("Night");
                        t.setAnimation(topAnim);

                    }
                    else if(event.values[0] >= (maxvalue/6) && event.values[0] < (2 * maxvalue/6))
                    {
                        imgView.setImageResource(R.drawable.sky);
                        t.setText("Day");
                        t.setAnimation(topAnim);

                    }
                    else if(event.values[0] >= (2 * maxvalue/6) && event.values[0] <= maxvalue)
                    {
                        imgView.setImageResource(R.drawable.sky_morning);
                        t.setText("Morning");
                        t.setAnimation(topAnim);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(lightListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightListener);
    }



}
