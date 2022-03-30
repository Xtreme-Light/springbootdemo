package com.example.switcher;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.springframework.web.servlet.HandlerInterceptor;

public class DataSourceHandlerInterceptor implements HandlerInterceptor {

  private HintManager hintManager;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    hintManager = HintManager.getInstance();
    final String tenant = request.getHeader("tenant");
    hintManager.setDatabaseShardingValue(tenant);
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (hintManager != null) {
      hintManager.clearShardingValues();
      hintManager.close();
    }
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
