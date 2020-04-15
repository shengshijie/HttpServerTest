
package com.shengshijie.httpservertest.controller;


import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.shengshijie.httpserver.http.Controller;
import com.shengshijie.httpserver.http.HttpFullRequest;
import com.shengshijie.httpserver.http.IController;
import com.shengshijie.httpserver.http.RawResponse;
import com.shengshijie.httpserver.http.RequestMapping;
import com.shengshijie.httpservertest.controller.requset.SetAmountRequest;

import org.jetbrains.annotations.NotNull;

@AutoService(IController.class)
@Controller
@RequestMapping(value = "/pay")
public class HelloWorldHandler implements IController {

    @NotNull
    @RequestMapping(value = "/setAmount", method = "POST")
    public RawResponse<Object> hello(HttpFullRequest request) {
        SetAmountRequest setAmountRequest = new Gson().fromJson(request.contentText(), SetAmountRequest.class);
        if (setAmountRequest.getAmount() > 10) {
            return RawResponse.ok("hello");
        } else {
            return RawResponse.fail("ddd");
        }
    }

    @NotNull
    @RequestMapping(value = "/hello2", method = "GET")
    public RawResponse<Object> hello2(HttpFullRequest request) {
        SetAmountRequest setAmountRequest = new Gson().fromJson(request.contentText(), SetAmountRequest.class);
        if (setAmountRequest.getAmount() > 10) {
            return RawResponse.ok("hello2");
        } else {
            return RawResponse.fail("ddd");
        }
    }

}
