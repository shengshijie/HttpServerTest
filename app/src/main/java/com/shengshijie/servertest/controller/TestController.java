package com.shengshijie.servertest.controller;

import com.shengshijie.server.http.annotation.Controller;
import com.shengshijie.server.http.annotation.RequestMapping;
import com.shengshijie.server.http.annotation.RequestParam;
import com.shengshijie.server.http.exception.BusinessException;
import com.shengshijie.server.http.response.SerializedResponse;

@Controller
@RequestMapping(value = "/java")
public class TestController {

    @RequestMapping(value = "/get1", method = "GET")
    public Object test(@RequestParam(value = "age") String age) throws BusinessException {
        if(age.equals("18")){
            throw new BusinessException("ddd");
        }
       return new SerializedResponse(null);
    }

}
