package com.example.alex_.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import static android.R.layout.simple_spinner_item;

public class AddCarActivity extends Activity {
    public String AddCarStr;
    public String AddTankVolumeStr;
    public String HowMuchOilStr;
    public String WhatMileageStr;
    public ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcar);
        String[] data = {"one", "two", "three", "four", "five"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final TextView AddCarText = (TextView) findViewById(R.id.AddCarText);
        final TextView AddTankVolumeText = (TextView) findViewById(R.id.AddTankVolumeText);
        final TextView HowMuchOilText = (TextView) findViewById(R.id.HowMuchOilText);
        final TextView WhatMileageText = (TextView) findViewById(R.id.WhatMileageText);
        final Spinner Cars = (Spinner) findViewById(R.id.Cars);
        Button SaveCarBtn = (Button) findViewById(R.id.SaveCarBtn);
        SaveCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarStr = AddCarText.getText().toString();
                AddTankVolumeStr = AddTankVolumeText.getText().toString();
                HowMuchOilStr =HowMuchOilText.getText().toString();
                WhatMileageStr = WhatMileageText.getText().toString();
                if (!AddCarStr.isEmpty() & !AddTankVolumeStr.isEmpty() & !HowMuchOilStr.isEmpty() & !WhatMileageStr.isEmpty()){
                  //  Cars.setAdapter(adapter);
                    Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
