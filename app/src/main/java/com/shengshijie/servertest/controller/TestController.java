package com.shengshijie.servertest.controller;

import com.shengshijie.server.http.annotation.Controller;
import com.shengshijie.server.http.annotation.Param;
import com.shengshijie.server.http.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/java")
public class TestController {

    @RequestMapping(value = "/get1", method = "GET")
    public void test(@Param(value = "age") String age) {
//        return "age:" + age;
    }

}
