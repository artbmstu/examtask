package ru.artbmstu.examtask.api.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class SuccessControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  public void testParallelRequestsFromDifferentIPs() throws Exception {
    int numRequests = 30;
    List<String> ips = new ArrayList<>();
    ips.add("192.168.0.1");
    ips.add("192.168.0.2");
    ips.add("192.168.0.3");

    ExecutorService executorService = Executors.newFixedThreadPool(numRequests);
    AtomicInteger successCount = new AtomicInteger(0);

    for (int i = 0; i < numRequests; i++) {
      String ip = ips.get(i % ips.size());
      executorService.execute(
          () -> {
            try {
              ResultActions resultActions =
                  mockMvc.perform(
                      MockMvcRequestBuilders.get("/success").header("X-Forwarded-For", ip));

              int statusCode = resultActions.andReturn().getResponse().getStatus();

              if (statusCode == HttpStatus.OK.value()) {
                successCount.incrementAndGet();
              }
            } catch (Exception ignored) {
            }
          });
    }

    executorService.shutdown();
    executorService.awaitTermination(1, TimeUnit.MINUTES);

    assertEquals(6, successCount.get());
  }
}
