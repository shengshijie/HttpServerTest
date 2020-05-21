package com.shengshijie.servertest.controller;

import com.shengshijie.server.http.Pair;
import com.shengshijie.server.http.annotation.Controller;
import com.shengshijie.server.http.annotation.Param;
import com.shengshijie.server.http.annotation.RequestMapping;
import com.shengshijie.server.http.exception.BusinessException;


@Controller
@RequestMapping(value = "/java")
public class TestController {

    @RequestMapping(value = "/get1", method = "GET")
    public Object test(@Param(value = "age") String age) {
       return new Pair<String,Object>("哈哈哈",new BusinessException("age"));
    }

}
