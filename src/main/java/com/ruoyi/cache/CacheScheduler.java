package com.ruoyi.cache;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public enum CacheScheduler {

    INSTANCE;

    private AtomicInteger cacheTaskNumber = new AtomicInteger(1);
    private ScheduledExecutorService scheduler;

    CacheScheduler() {
        this.shutdown();
        this.scheduler = new ScheduledThreadPoolExecutor(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("shield-Task-%s", cacheTaskNumber.getAndIncrement()));
            }
        });
    }

    private void shutdown() {
        if (null != scheduler) {
            this.scheduler.shutdown();
        }
    }

    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return this.scheduler.schedule(task, delay, unit);
    }
}