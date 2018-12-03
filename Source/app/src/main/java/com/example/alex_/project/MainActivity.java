package com.example.alex_.project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.OPEN_READWRITE;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase db;
    public ArrayAdapter<String> adapter;
    public ArrayList<String> myList;
    public String floodedStr;
    public String mileageStr;
    public String afterDriveStr;

    public MainActivity() {
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Spinner Cars = (Spinner) findViewById(R.id.Cars);
        myList = new ArrayList<String>();

        db = getBaseContext().openOrCreateDatabase("app.db", OPEN_READWRITE , null);
        db.execSQL("CREATE TABLE IF NOT EXISTS car (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT NOT NULL, tankvolume INTEGER NOT NULL, " +
                "mileage INTEGER NOT NULL, lastmileage INTEGER, " +
                "intanklast INTEGER, intanknow INTEGER NOT NULL, flooded INTEGER, average DOUBLE)");
        final Cursor cursor = db.rawQuery("SELECT * FROM car;", null);
        final TextView floodedNum = (TextView) findViewById(R.id.FloodedNumber);
        final TextView mileageNum = (TextView) findViewById(R.id.MileageNumber);
        final TextView afterDrive = (TextView) findViewById(R.id.afterDrive);
        final TextView tankVolLabel = (TextView) findViewById(R.id.TankVolumeLabel);
        final TextView inTankLabel = (TextView) findViewById(R.id.InTankLabel);
        final TextView mileageLabel = (TextView) findViewById(R.id.MileageLabel);
        final TextView consumptionLabel = (TextView) findViewById(R.id.consumptionLabel);
        if(cursor.getCount() == 0){
            Intent intent = new Intent(MainActivity.this, AddCarActivity.class);
            startActivity(intent);
        }
        int count = cursor.getCount();
        if(cursor.getCount() > 0){
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                String carName = cursor.getString(1);
                myList.add(carName);
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Cars.setAdapter(adapter);
        Cars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(cursor.getCount() > 0){
                    int index = Cars.getSelectedItemPosition();
                    String getName = myList.get(index);
                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                        String carName = cursor.getString(1);
                        if(carName.equals(getName)){
                            tankVolLabel.setText("Объем бака: " + cursor.getString(2));
                            inTankLabel.setText("В баке осталось: " + cursor.getString(5));
                            mileageLabel.setText("Пробег: " + cursor.getString(4));
                            String formatedDouble = String.format("%.1f", Double.parseDouble(cursor.getString(8)));
                            consumptionLabel.setText(formatedDouble + "л/100км");
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addCarBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, AddCarActivity.class);
                startActivity(intent);
            }
        });
        Button enterBtn = (Button) findViewById(R.id.EnterBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterDriveStr = afterDrive.getText().toString();
                floodedStr = floodedNum.getText().toString();
                mileageStr = mileageNum.getText().toString();
                if(!floodedStr.isEmpty() & !mileageStr.isEmpty()){
                    int afterDriveInt = Integer.parseInt(afterDriveStr);
                    int floodedInt = Integer.parseInt(floodedStr);
                    int mileageInt = Integer.parseInt(mileageStr);
                    int index = Cars.getSelectedItemPosition();
                    String getName = myList.get(index);
                    final Cursor newCursor = db.rawQuery("SELECT * FROM car;", null);
                    if(newCursor.getCount() > 0){
                        for(newCursor.moveToFirst(); !newCursor.isAfterLast(); newCursor.moveToNext()){
                            String carName = newCursor.getString(1);
                            int id =Integer.parseInt(newCursor.getString(0));
                            if(carName.equals(getName)){
                                int lastTank = Integer.parseInt(newCursor.getString(6));
                                int lastMileage = Integer.parseInt(newCursor.getString(3));
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("intanklast", afterDriveInt );
                                contentValues.put("mileage", mileageInt);
                                contentValues.put("lastmileage", lastMileage);
                                contentValues.put("flooded", floodedInt);
                                int res = floodedInt + afterDriveInt;
                                contentValues.put("intanknow", res);
                                //db.update("car", contentValues, "id=" + id, null);
                                int resTank = lastTank - afterDriveInt;
                                int resMileage = mileageInt - lastMileage;
                                double resKM = (double) resTank/resMileage;
                                double resultCons = resKM*100;
                                String formatedDouble = String.format("%.1f", resultCons);
                                //ContentValues averageValue = new ContentValues();
                                contentValues.put("average", resultCons);
                                db.update("car", contentValues, "id=" + id, null);
                                afterDrive.setText("");
                                floodedNum.setText("");
                                mileageNum.setText("");
                                tankVolLabel.setText("");
                                inTankLabel.setText("");
                                mileageLabel.setText("");
                                tankVolLabel.setText("Объем бака: " + newCursor.getString(2));
                                inTankLabel.setText("В баке осталось: " + afterDriveInt);
                                mileageLabel.setText("Пробег: " + mileageInt);
                                consumptionLabel.setText(formatedDouble + "л/100км");

                            }
                        }
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
