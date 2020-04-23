package com.shengshijie.httpservertest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shengshijie.httpserver.HttpServer;
import com.shengshijie.log.HLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HLog.init(this,"");
        findViewById(R.id.btn_start).setOnClickListener(v -> HttpServer.start(8888, (i,s) -> {
            HLog.log(i+5,s);
            return null;
        }));
    }
}
