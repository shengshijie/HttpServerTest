package com.shengshijie.servertest.controller;

import com.shengshijie.server.http.annotation.Controller;
import com.shengshijie.server.http.annotation.Param;
import com.shengshijie.server.http.annotation.RequestMapping;
import com.shengshijie.server.http.exception.BusinessException;


@Controller
@RequestMapping(value = "/java")
public class TestController {

    @RequestMapping(value = "/get1", method = "GET")
    public Object test(@Param(value = "age") String age) throws BusinessException {
        if(age.equals("18")){
            throw new BusinessException("ddd");
        }
       return new BusinessException("age",22);
    }

}
