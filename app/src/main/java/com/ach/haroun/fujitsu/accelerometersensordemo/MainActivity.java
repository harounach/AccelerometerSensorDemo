package com.ach.haroun.fujitsu.accelerometersensordemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    TextView speedText;
    SensorManager mSensorManager;
    Sensor mSensor;
    float old_x, old_y, old_z;
    long lastUpdate;
    int SHAKE_THRESHOLD = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speedText = (TextView) findViewById(R.id.text_speed_id);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long currentTime = System.currentTimeMillis();
        if((currentTime-lastUpdate)>100){
            long time = currentTime - lastUpdate;
            lastUpdate = currentTime;
            float speed = Math.abs(x+y+z-old_x-old_y-old_z)/time*10000;
            if(speed>SHAKE_THRESHOLD){
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                speedText.setText("Shake Speed: " +speed);
            }
            old_x = x;
            old_y = y;
            old_z = z;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
