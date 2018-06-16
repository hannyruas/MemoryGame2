package com.example.hannybuns.memorygame6.service;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

public class MotionService extends Service implements SensorEventListener {
    public static final String MOTION_CHANGE = "MOTION_CHANGE";
    public static final String VALIDITY = "VALIDITY";

    private SensorManager sensorManager;
    private Sensor mRotationSensor;

    private Handler serviceHandler;

    protected SensorServiceBinder sensorServiceBinder = new SensorServiceBinder();// An IBinder implementation subclass


    private static final int SENSOR_DELAY = 500 * 1000; // 500ms
    private boolean hasDefaultVect = false;
    private boolean isValid = true;
    private float firstPitch;
    private float firstRoll;
    private float firstAzimut;
    private float[] currentOrientain = new float[3];

    HandlerThread sensorThread;
    boolean isListening = false;


    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        mRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);

        sensorThread = new HandlerThread(MotionService.class.getSimpleName());
        sensorThread.start();
        serviceHandler = new Handler(sensorThread.getLooper());


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
        sensorThread.quit();

    }

    @Override
    public IBinder onBind(Intent intent) {

        sensorServiceBinder.sensorService = this;

        return sensorServiceBinder;

    }

    @Override
    public boolean onUnbind(Intent intent) {

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mRotationSensor && event.accuracy > 2) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                currentOrientain = getOrientaionFromEvent(truncatedRotationVector);
            } else {
                currentOrientain = getOrientaionFromEvent(event.values);
            }
            if (!hasDefaultVect) {
                setDefaultVect();
            }
            boolean tempRotation = checkValidtyRotaionAngle();
            if (tempRotation != isValid) {
                Log.d("motion Handler", "Detection");
                isValid = tempRotation;
                //TODO notify
                sendMotionDetection(tempRotation);
            }
        }
    }

    private boolean checkValidtyRotaionAngle() {
        float azimuth = currentOrientain[0];
        float pitch = currentOrientain[1];
        float roll = currentOrientain[2];
        if (hasDefaultVect) {
            if (Math.abs(firstPitch - pitch) > 0.4
                    || Math.abs(firstRoll - roll) > 1.5
                    || Math.abs(firstAzimut - azimuth) > 1.5) {

                return true;
            }
        }
        return false;

    }

    private float[] getOrientaionFromEvent(float[] eventValues) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, eventValues);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        return orientation;
    }

    private void sendMotionDetection(boolean detect) {
        // The string "my-integer" will be used to filer the intent
        Intent intent = new Intent(MOTION_CHANGE);
        // Adding some data
        intent.putExtra(VALIDITY, detect);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void setDefaultVect() {
        if (!hasDefaultVect) {
            this.firstAzimut = currentOrientain[0];
            this.firstPitch = currentOrientain[1];
            this.firstRoll = currentOrientain[2];
            hasDefaultVect = true;
        }
    }

    public class SensorServiceBinder extends Binder {
        public static final String START_LISTENING = "Start";

        private MotionService sensorService;

        MotionService getService() {
            return sensorService;
        }

        public void notifyService(String msg) {
            if (msg == SensorServiceBinder.START_LISTENING && !isListening) {
                List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
                Log.d("", "Available sensors: " + sensorList);
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR); // Sensor.TYPE_GYROSCOPE will be null in Genymotion free edition
                if (sensor == null && sensorList.size() > 0) {
                    // Backup
                    Log.d("", "PROBLEMMMMMM"); //if is not a real device the service will not work
                }

                if (sensor == null) return;

                sensorManager.registerListener(getService(), sensor, SensorManager.SENSOR_DELAY_UI, serviceHandler);
                isListening = true;
            }


        }
    }
}