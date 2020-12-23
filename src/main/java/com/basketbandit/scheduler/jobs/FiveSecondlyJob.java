package com.basketbandit.scheduler.jobs;

import com.basketbandit.scheduler.Job;
import com.basketbandit.scheduler.tasks.IsAliveTask;

import java.util.concurrent.TimeUnit;

public class FiveSecondlyJob extends Job {
    private final IsAliveTask isAliveTask = new IsAliveTask();

    public FiveSecondlyJob() {
        super(0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(isAliveTask);
    }
}
