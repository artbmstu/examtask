package ru.artbmstu.examtask.api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artbmstu.examtask.config.throttle.ThrottleRequest;

@RestController
public class SuccessController {

  @GetMapping("/success")
  @ThrottleRequest
  public ResponseEntity<String> getSuccessResponse() {
    return ResponseEntity.ok().build();
  }
}
