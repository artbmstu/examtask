package ru.artbmstu.examtask.config.throttle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RequestThrottlingInterceptor implements HandlerInterceptor {

  private final long period;
  private final int limit;
  private final TimeUnit timeUnit;

  public RequestThrottlingInterceptor(long period, int limit, TimeUnit timeUnit) {
    this.period = period;
    this.limit = limit;
    this.timeUnit = timeUnit;
  }

  private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    if (!(handler instanceof HandlerMethod)) return true;

    HandlerMethod handlerMethod = (HandlerMethod) handler;
    ThrottleRequest throttleRequest = handlerMethod.getMethodAnnotation(ThrottleRequest.class);
    if (throttleRequest != null) {
      String ip = getClientIP(request);
      TokenBucket bucket =
          buckets.computeIfAbsent(ip, k -> new TokenBucket(limit, period, timeUnit));
      if (!bucket.tryConsume()) {
        response.setStatus(HttpStatus.BAD_GATEWAY.value());
        return false;
      }
    }
    return true;
  }

  private String getClientIP(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader == null) {
      return request.getRemoteAddr();
    }
    return xForwardedForHeader.split(",")[0];
  }
}
