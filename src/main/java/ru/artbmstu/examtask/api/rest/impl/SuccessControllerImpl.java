package ru.artbmstu.examtask.api.rest.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.artbmstu.examtask.api.rest.SuccessController;
import ru.artbmstu.examtask.service.SuccessService;

@RestController
@RequiredArgsConstructor
public class SuccessControllerImpl implements SuccessController {

  private final SuccessService successService;

  @Override
  public ResponseEntity<String> getSuccessResponse() {
    return ResponseEntity.ok(successService.getSuccessResponse());
  }
}
