
package com.shengshijie.httpserver;

public interface IFunctionHandler<T> {

    RawResponse<T> execute(HttpRequest request);

}
