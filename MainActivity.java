package com.example.atique.divideimage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText numbers, duration;
    Button ok;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        numbers = (EditText) findViewById(R.id.numberofpieces);
        duration = (EditText) findViewById(R.id.duration);
        ok = (Button) findViewById(R.id.ok);

        if(numbers.getText().toString() != "" && duration.getText().toString() != "") {
            ok.setEnabled(true);
        }

        else {
            Toast.makeText(MainActivity.this, "fields cannot be empty", Toast.LENGTH_SHORT).show();
        }

        ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(numbers.getText()) && !TextUtils.isEmpty(duration.getText())){
                    Intent intent = new Intent(MainActivity.this, Animations.class);
                    intent.putExtra("number", numbers.getText().toString());
                    intent.putExtra("duration", duration.getText().toString());
                    startActivity(intent);
                    finish();
                    }
                    else{
                        numbers.setError("Enter value");
                        duration.setError("Enter value");
                    }
                }
            });
        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 6);
        }
        ImagesData createTable = new ImagesData();
        createTable.fetchImages(MainActivity.this);

    }
    }