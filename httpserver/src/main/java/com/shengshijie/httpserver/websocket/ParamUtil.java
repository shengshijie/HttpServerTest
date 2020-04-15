package com.shengshijie.httpserver.websocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.http.HttpRequest;
public class ParamUtil {

    public static Map<String, String> getRequestParams(HttpRequest request){
        Map<String, String>requestParams=new HashMap<>();
        if (request.method() == HttpMethod.GET) {
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> params = decoder.parameters();
            for(Map.Entry<String, List<String>> entry : params.entrySet()){
                requestParams.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        if (request.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for(InterfaceHttpData data : postData){
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    requestParams.put(attribute.getName(), attribute.getValue());
                }
            }
        }
        return requestParams;
    }

    public static FileUpload getFileUpload(HttpRequest request){
        if (request.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for(InterfaceHttpData data : postData){
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                    FileUpload fileUpload = (FileUpload) data;
                    return fileUpload;
                }
            }
        }
        return null;
    }

    public static String getUri(HttpRequest request){
        return new QueryStringDecoder(request.uri()).path();
    }

}