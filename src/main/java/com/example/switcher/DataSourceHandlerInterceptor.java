package com.example.switcher;


import com.example.holder.HintContextHolder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class DataSourceHandlerInterceptor implements HandlerInterceptor {

  private HintContextHolder hintContextHolder;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    final String tenant = request.getHeader("tenant");
    hintContextHolder = new HintContextHolder();
    hintContextHolder.pushDatabaseShardingValue(tenant);
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (hintContextHolder != null) {
      hintContextHolder.clear();
    }
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
