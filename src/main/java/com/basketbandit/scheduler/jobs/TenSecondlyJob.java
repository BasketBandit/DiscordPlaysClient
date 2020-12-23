package com.basketbandit.scheduler.jobs;

import com.basketbandit.scheduler.Job;
import com.basketbandit.scheduler.tasks.IsAliveTask;

import java.util.concurrent.TimeUnit;

public class TenSecondlyJob extends Job {
    private final IsAliveTask isAliveTask = new IsAliveTask();

    public TenSecondlyJob() {
        super(0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(isAliveTask);
    }
}
