package com.example.cameraflashlight.demoone;// src/main/java/your/package/name/MainActivity.java

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cameraflashlight.R;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton toggleButton = findViewById(R.id.toggleButton);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (!hasFlash()) {
            // Handle the scenario where the device doesn't have a camera flash
            // You may want to show an error message or disable the toggle button
            toggleButton.setEnabled(false);
        } else {
            try {
                cameraId = cameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                // Handle the exception if getting the camera ID fails
                e.printStackTrace();
            }

            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        turnOnFlashlight();
                    } else {
                        turnOffFlashlight();
                    }
                }
            });
        }
    }

    private boolean hasFlash() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void turnOnFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            // Handle the exception if turning on the flashlight fails
            e.printStackTrace();
        }
    }

    private void turnOffFlashlight() {
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            // Handle the exception if turning off the flashlight fails
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Ensure flashlight is turned off when the app is paused
        turnOffFlashlight();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure flashlight is turned off when the app is closed
        turnOffFlashlight();
    }
}
