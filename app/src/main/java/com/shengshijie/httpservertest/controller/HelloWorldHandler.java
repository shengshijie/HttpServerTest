
package com.shengshijie.httpservertest.controller;


import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.shengshijie.httpserver.Controller;
import com.shengshijie.httpserver.HttpRequest;
import com.shengshijie.httpserver.IController;
import com.shengshijie.httpserver.RawResponse;
import com.shengshijie.httpserver.RequestMapping;
import com.shengshijie.httpservertest.controller.requset.SetAmountRequest;

import org.jetbrains.annotations.NotNull;

@AutoService(IController.class)
@Controller
@RequestMapping(value = "/pay")
public class HelloWorldHandler implements IController {

    @NotNull
    @RequestMapping(value = "/setAmount", method = "POST")
    public RawResponse<Object> hello(HttpRequest request) {
        SetAmountRequest setAmountRequest = new Gson().fromJson(request.contentText(), SetAmountRequest.class);
        if (setAmountRequest.getAmount() > 10) {
            return RawResponse.ok("hello");
        } else {
            return RawResponse.fail("ddd");
        }
    }

    @NotNull
    @RequestMapping(value = "/hello2", method = "GET")
    public RawResponse<Object> hello2(HttpRequest request) {
        SetAmountRequest setAmountRequest = new Gson().fromJson(request.contentText(), SetAmountRequest.class);
        if (setAmountRequest.getAmount() > 10) {
            return RawResponse.ok("hello2");
        } else {
            return RawResponse.fail("ddd");
        }
    }

}
