package ru.artbmstu.examtask.service.impl;

import org.springframework.stereotype.Service;
import ru.artbmstu.examtask.config.throttle.aop.ThrottleRequest;
import ru.artbmstu.examtask.service.SuccessService;

@Service
public class SuccessServiceImpl implements SuccessService {

  @ThrottleRequest
  @Override
  public String getSuccessResponse() {
    return "";
  }
}
