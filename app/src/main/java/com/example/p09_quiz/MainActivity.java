package com.example.p09_quiz;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    String folderLocation;
    Button btnSave , btnRead , btnShow;
    EditText etCor;
    TextView tvRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSave = findViewById(R.id.btnSave);
        etCor = findViewById(R.id.etCor);
        btnRead = findViewById(R.id.btnRead);
        tvRead = findViewById(R.id.tvRead);
        btnShow = findViewById(R.id.btnShow);


        if(checkPermission()){
            String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
            File folder = new File(folderLocation);
            if (folder.exists() == false){
                boolean result = folder.mkdir();
                if (result == true){
                    Log.d("FileRead/Write", "Folder created");
                }
            }
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Code for file writing
                String msg = etCor.getText().toString();
                try {
                    folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                    File targetFile = new File(folderLocation , "quiz.txt");
                    FileWriter writer = new FileWriter(targetFile , false);
                    writer.write(msg);
                    writer.flush();
                    writer.close();
                    Toast.makeText(MainActivity. this , "Added!", Toast. LENGTH_LONG ).show();
                } catch (Exception e){
                    Toast.makeText(MainActivity. this , "Failed to write!", Toast. LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                File targetFile = new File(folderLocation , "quiz.txt");
                if(targetFile.exists() == true){
                    String data = "";
                    try{
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();

                            data = line;

                        tvRead.setText(data);
                        br.close();
                        reader.close();
                    }
                    catch(Exception e){
                        Toast.makeText(MainActivity.this , "Failed to read!", Toast. LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    Log.d("Content", data);
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coordinates = tvRead.getText().toString();
                String[] cor = coordinates.split(",");
                Intent i = new Intent(MainActivity.this, Map.class);
                i.putExtra("cor", cor);
                startActivity(i);
            }
        });


    }



    private boolean checkPermission(){
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                && permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return false;
        }
    }
}
