package com.shengshijie.servertest.controller;

import com.shengshijie.server.http.RawResponse;
import com.shengshijie.server.http.annotation.Controller;
import com.shengshijie.server.http.annotation.Param;
import com.shengshijie.server.http.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/test")
public class TestController {

    @RequestMapping(value = "/hhh", method = "GET")
    public RawResponse<Object> test(@Param(value = "age") String age) {
        return RawResponse.ok("age:" + age);
    }

}
