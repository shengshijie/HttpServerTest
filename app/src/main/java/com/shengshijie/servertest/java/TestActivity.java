package com.shengshijie.servertest.java;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.shengshijie.server.ServerManager;
import com.shengshijie.server.http.config.ServerConfig;
import com.shengshijie.server.platform.android.AndroidServer;
import com.shengshijie.servertest.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.start).setOnClickListener(v -> {
            List<String> list = new ArrayList<>();
            list.add("com.shengshijie.httpservertest.controller");
            ServerConfig serverConfig = new ServerConfig.Builder()
                    .setServer(new AndroidServer(this))
                    .setPort(8888)
                    .setPackageNameList(list)
                    .build();
            ServerManager.start(serverConfig);
        });
    }
}
