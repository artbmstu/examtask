package ru.artbmstu.examtask.config.throttle.aop;

import java.util.concurrent.TimeUnit;

public class TokenBucket {
  private final int capacity;
  private final long periodNanos;
  private long lastRefillTime;
  private long nextRefillTime;
  private long tokens;

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
    if (now < nextRefillTime) {
      return;
    }
    long timePassed = now - lastRefillTime;
    long numPeriods = timePassed / periodNanos;
    lastRefillTime = now;
    nextRefillTime = lastRefillTime + periodNanos;
    tokens = Math.min(tokens + numPeriods * capacity, capacity);
  }
}
