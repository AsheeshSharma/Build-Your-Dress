package com.animelabs.asheeshsharma.dressupapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.animelabs.asheeshsharma.dressupapp.R;
import com.animelabs.asheeshsharma.dressupapp.Utility.MarshPermission;
import com.animelabs.asheeshsharma.dressupapp.Utility.SharedPref;

/**
 * Created by Asheesh.Sharma on 05-12-2016.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        MarshPermission marshPermission = new MarshPermission(SplashActivity.this);
        if(!marshPermission.isStoragePermissionGranted()){
            Toast.makeText(getApplicationContext(),"Please accept the permissions",Toast.LENGTH_SHORT).show();
        }
        if(SharedPref.getSplashShown(getApplicationContext())!=0){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(3800);
                        SharedPref.setSplashShown(getApplicationContext());
                    } catch (InterruptedException e) {
                        e.setStackTrace(getStackTrace());
                    } finally {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
            };
            timer.start();
        }

    }

}
