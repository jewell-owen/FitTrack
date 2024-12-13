package com.example.fittrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StepCounter extends AppCompatActivity {

    private SensorManager sensorManager;

    private Button b_stepGraph;
    private Sensor accelerometer;
    private int stepCount = 6000;
    private boolean isStepCounting = false;
    private TextView stepCountTextView;
    private ProgressBar progressBar;
    private TextView progressText;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);        }



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);
        b_stepGraph = (Button) findViewById(R.id.b_steps);
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        progressText.setText(Integer.toString(stepCount));
        progressBar.setProgress(stepCount/100);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {

        /**
         *
         * Initilize the Step counter
         * @param sensorEvent
         */
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Handler handler = new Handler();

//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    // yourMethod();
//                }
//            }, 1000);   //1 seconds
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            double acceleration = Math.sqrt(x * x + y * y + z * z);


            if (sensorEvent.values[0] > 1 ) {
                isStepCounting = true;
                stepCount++;
                stepCountTextView.setText("Step Count: " + stepCount);

                progressText.setText(Integer.toString(i));
                progressBar.setProgress(i);
                i++;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
//        }
//    }
//    @Override
//    protected  void onPause() {
//        super.onPause();
//        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
//            sensorManager.unregisterListener(this, stepCounter);
//        }
//    }


    /**
     * request permission for step counter
     *
     * @param requestCode The request code passed in {@link #requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. You can now work with location.
            } else {
                // Permission denied.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Go to the Step graph
     * @param view
     */
    public void onStepCounter(View view) {
        Intent intent;
        intent = new Intent(this, StepGraph.class);
        startActivity(intent);
    }

}
