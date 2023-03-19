package ru.artbmstu.examtask.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.artbmstu.examtask.config.throttle.RequestThrottlingInterceptor;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${throttling.period:10}")
  private long period;

  @Value("${throttling.limit:3}")
  private int limit;

  @Value("${throttling.timeUnit:MINUTES}")
  private TimeUnit timeUnit;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestThrottlingInterceptor(period, limit, timeUnit));
  }
}
