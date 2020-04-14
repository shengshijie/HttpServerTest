package com.shengshijie.httpservertest.controller;

import com.google.auto.service.AutoService;
import com.shengshijie.httpserver.HttpRequest;
import com.shengshijie.httpserver.IFunctionHandler;
import com.shengshijie.httpserver.RawResponse;
import com.shengshijie.httpserver.RequestMapping;

import org.jetbrains.annotations.NotNull;

@AutoService(IFunctionHandler.class)
@RequestMapping(path = "/request/body", method = "POST")
public class RequestBodyHandler implements IFunctionHandler<String> {
    @NotNull
    @Override
    public RawResponse<String> execute(HttpRequest request) {
        String json = request.contentText();
        return RawResponse.ok(json);
    }
}
