
package com.shengshijie.httpservertest.controller;

import com.google.auto.service.AutoService;
import com.shengshijie.httpserver.HttpRequest;
import com.shengshijie.httpserver.IFunctionHandler;
import com.shengshijie.httpserver.RawResponse;
import com.shengshijie.httpserver.RequestMapping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@AutoService(IFunctionHandler.class)
@RequestMapping(path = "/moment/list/",equal = false)
public class PathVariableHandler implements IFunctionHandler<List<HashMap<String,String>>> {
    @Override
    public RawResponse<List<HashMap<String,String>>> execute(HttpRequest request) {

//        String id = request.getStringPathValue(3);
        List<HashMap<String,String>> list = new LinkedList<>();
        HashMap<String,String> data = new HashMap<>();
        data.put("id","1");
        data.put("name","Bluesky");
        data.put("text","hello sea!");
        data.put("time","2018-08-08 08:08:08");
        list.add(data);
        return RawResponse.ok(list);
    }
}
