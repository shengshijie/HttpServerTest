package com.shengshijie.servertest.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shengshijie.servertest.R;
import com.shengshijie.server.ServerManager;
import com.shengshijie.server.http.config.ServerConfig;
import com.shengshijie.server.platform.android.AndroidServer;

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
