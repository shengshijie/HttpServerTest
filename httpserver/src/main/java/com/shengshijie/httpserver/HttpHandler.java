package com.shengshijie.httpserver;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ReferenceCountUtil;

@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private HashMap<Path, IFunctionHandler> functionHandlerMap = new HashMap<>();

    private ExecutorService executor = Executors.newCachedThreadPool(runnable -> {
        Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setName("netty-" + thread.getName());
        return thread;
    });

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        FullHttpRequest copyRequest = request.copy();
        executor.execute(() -> onReceivedRequest(ctx, new HttpRequest(copyRequest)));
    }

    private void onReceivedRequest(ChannelHandlerContext context, HttpRequest request) {
        FullHttpResponse response = handleHttpRequest(request);
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        ReferenceCountUtil.release(request);
    }

    private FullHttpResponse handleHttpRequest(HttpRequest request) {
        IFunctionHandler functionHandler;
        try {
            functionHandler = matchFunctionHandler(request);
            RawResponse rawResponse = functionHandler.execute(request);
            return HttpResponse.ok(rawResponse.toJSONString());
        } catch (MethodNotAllowedException error) {
            return HttpResponse.make(HttpResponseStatus.METHOD_NOT_ALLOWED);
        } catch (PathNotFoundException error) {
            return HttpResponse.make(HttpResponseStatus.NOT_FOUND);
        } catch (Exception error) {
            return HttpResponse.makeError(error);
        }
    }

    public void registerRouter() {
        for (IFunctionHandler handler : ServiceLoader.load(IFunctionHandler.class)) {
            Path path = Path.make(handler.getClass().getAnnotation(RequestMapping.class));
            if (!functionHandlerMap.containsKey(path)) {
                functionHandlerMap.put(path, handler);
            }
        }
    }

    private IFunctionHandler matchFunctionHandler(HttpRequest request) throws PathNotFoundException, MethodNotAllowedException {
        Path requestPath = null;
        boolean methodAllowed = false;
        for (Map.Entry<Path, IFunctionHandler> entry : functionHandlerMap.entrySet()) {
            Path path = entry.getKey();
            if (request.matched(path.getUri(), path.isEqual())) {
                requestPath = path;
                if (request.isAllowed(path.getMethod())) {
                    methodAllowed = true;
                }
            }
        }
        if (requestPath == null) {
            throw new PathNotFoundException();
        }
        if (!methodAllowed) {
            throw new MethodNotAllowedException();
        }
        return functionHandlerMap.get(requestPath);
    }

}
