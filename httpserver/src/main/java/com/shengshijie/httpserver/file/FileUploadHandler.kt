package com.shengshijie.httpserver.file

import com.shengshijie.httpserver.websocket.ParamUtil
import com.shengshijie.httpserver.websocket.ResponseUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import java.io.File

class FileUploadHandler : SimpleChannelInboundHandler<FileUploadRequestVo>() {
    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, requestVo: FileUploadRequestVo) {
        val request = requestVo.request
        val fileUpload = ParamUtil.getFileUpload(request)
        if (fileUpload == null || !fileUpload.isCompleted) {
            ResponseUtil.sendHttpResponse(ctx, request, ResponseUtil.get400Response())
            return
        }
        val fileName = fileUpload.filename
        val newName = System.currentTimeMillis().toString() + fileName.substring(fileName.lastIndexOf("."))
        fileUpload.renameTo(File("E:/tmp/$newName"))
        ResponseUtil.sendHttpResponse(ctx, request, ResponseUtil.get200Response("http://localhost:8090/file/$newName"))
    }
}