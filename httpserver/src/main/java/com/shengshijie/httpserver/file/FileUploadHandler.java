package com.shengshijie.httpserver.file;

import com.shengshijie.httpserver.websocket.ParamUtil;
import com.shengshijie.httpserver.websocket.ResponseUtil;

import java.io.File;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.FileUpload;

public class FileUploadHandler extends SimpleChannelInboundHandler<FileUploadRequestVo> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileUploadRequestVo requestVo) throws Exception {
        FullHttpRequest request = requestVo.getRequest();
        FileUpload fileUpload = ParamUtil.getFileUpload(request);
        if(fileUpload == null || ! fileUpload.isCompleted()){
            ResponseUtil.sendHttpResponse(ctx, request, ResponseUtil.get400Response());
            return;
        }
        String fileName = fileUpload.getFilename();
        String newName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
        fileUpload.renameTo(new File("E:/tmp/" + newName));
        ResponseUtil.sendHttpResponse(ctx, request, ResponseUtil.get200Response("http://localhost:8090/file/" + newName));
    }

}