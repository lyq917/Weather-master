package com.orzmo.weather;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orzmo.weather.Location.District;
import com.orzmo.weather.Location.DistrictFather;
import com.orzmo.weather.utils.CallBack;
import com.orzmo.weather.utils.DistrictHelper;
import com.orzmo.weather.utils.JsonToDistrict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class DistrictActivity extends AppCompatActivity {
    private static final String TAG = "DistrictActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.initView();
    }


    private void initView() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                DistrictHelper districtHelper = new DistrictHelper(new CallBack() {
                    @Override
                    public void run (String s) {
                        getNewDistrict(s);
                    }
                });
                districtHelper.getFirstDistrict();
            }
        }).start();
    }


    private void getNewDistrict(final String s) {
        //获取市区
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                JsonToDistrict jtd = new JsonToDistrict(s);
                final DistrictFather districts = jtd.getDistricts();

                String[] data = new String[districts.getLength()];
                int i = 0;
                for (District item : districts.getDistricts()) {
                    data[i] = item.getName();
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DistrictActivity.this, android.R.layout.simple_list_item_1,data);
                ListView lv = (ListView) findViewById(R.id.list_view1);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        District d = districts.getDistricts().get(i);
                        Log.d(TAG, d.getName());
                        handleClickDistrictFirst(d.getName());
                    }
                });

            }
        });


    }


    private void handleClickDistrictFirst(String cityName) {
        //处理点击事件
        Intent intent = new Intent(DistrictActivity.this, SecondDistrictActivity.class);
        intent.putExtra("cityName", cityName);
        startActivity(intent);
        finish();
    }


}
