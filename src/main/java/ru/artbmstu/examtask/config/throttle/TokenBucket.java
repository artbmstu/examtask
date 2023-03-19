package ru.artbmstu.examtask.config.throttle;

import java.util.concurrent.TimeUnit;

public class TokenBucket {
  private final int capacity;
  private final long periodNanos;
  private long lastRefillTime;
  private double tokens;

  public TokenBucket(int capacity, long period, TimeUnit timeUnit) {
    this.capacity = capacity;
    this.periodNanos = timeUnit.toNanos(period);
    this.tokens = capacity;
    this.lastRefillTime = System.nanoTime();
  }

  public synchronized boolean tryConsume() {
    refill();
    if (tokens >= 1) {
      tokens--;
      return true;
    } else {
      return false;
    }
  }

  private void refill() {
    long now = System.nanoTime();
    long timePassed = now - lastRefillTime;
    double newTokens = timePassed * capacity / (double) periodNanos;
    tokens = Math.min(tokens + newTokens, capacity);
    lastRefillTime = now;
  }
}
