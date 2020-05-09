package com.shengshijie.httpservertest.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.shengshijie.httpservertest.R;
import com.shengshijie.log.HLog;
import com.shengshijie.server.ServerManager;
import com.shengshijie.server.http.config.Config;
import com.shengshijie.server.platform.AndroidServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.start).setOnClickListener(v -> {
            Config config = Config.INSTANCE;
            config.setPort(8888);
            config.setServer(AndroidServer.INSTANCE.setContext(this));
            List<String> list = new ArrayList<>();
            list.add("com.shengshijie.httpservertest.controller");
            config.setPackageNameList(list);
            config.setLog((logLevel, s) -> {
                        HLog.log(logLevel.ordinal(), s);
                        return null;
                    }
            );
            ServerManager.INSTANCE.start(config);
        });
    }
}
