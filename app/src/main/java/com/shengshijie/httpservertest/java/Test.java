package com.shengshijie.httpservertest.java;

import com.shengshijie.log.HLog;
import com.shengshijie.server.ServerManager;
import com.shengshijie.server.http.config.Config;
import com.shengshijie.server.platform.AndroidServer;

import java.util.Collections;

public class Test {

    public static void main(String[] args) {
        Config config = Config.INSTANCE;
        config.setPort(8888);
        config.setServer(AndroidServer.INSTANCE.setContext(null));
        config.setPackageNameList(Collections.singletonList("com.shengshijie.httpservertest.controller"));
        config.setLog((logLevel, s) -> {
                    HLog.log(logLevel.ordinal(), s);
                    return null;
                }
        );
        ServerManager.INSTANCE.start(config);
    }
}
