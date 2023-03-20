package ru.artbmstu.examtask.config.throttle.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RequestThrottlingAspect {

  private final HttpServletRequest request;

  @Value("${throttling.period:10}")
  private long period;

  @Value("${throttling.limit:3}")
  private int limit;

  @Value("${throttling.timeUnit:MINUTES}")
  private TimeUnit timeUnit;

  private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

  @Before("@annotation(ru.artbmstu.examtask.config.throttle.aop.ThrottleRequest)")
  public void beforeMethod() {
    String ip = getClientIP(request);
    TokenBucket bucket = buckets.computeIfAbsent(ip, k -> new TokenBucket(limit, period, timeUnit));
    if (!bucket.tryConsume()) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Too many requests");
    }
  }

  private String getClientIP(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader == null) {
      return request.getRemoteAddr();
    }
    return xForwardedForHeader.split(",")[0];
  }
}
