package com.sky.android.aidl.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start_service).setOnClickListener(this);
        findViewById(R.id.btn_stop_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, DemoRemoteService.class);

        if (R.id.btn_start_service == view.getId()) {

            // 启动服务
            startService(intent);
        } else if (R.id.btn_stop_service == view.getId()) {

            // 停止服务
            stopService(intent);
        }
    }
}
