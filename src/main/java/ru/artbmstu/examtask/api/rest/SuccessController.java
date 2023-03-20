package ru.artbmstu.examtask.api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface SuccessController {

  @GetMapping("/success")
  ResponseEntity<String> getSuccessResponse();
}
