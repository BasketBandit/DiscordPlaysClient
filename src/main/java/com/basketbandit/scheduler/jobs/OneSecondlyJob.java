package com.basketbandit.scheduler.jobs;

import com.basketbandit.scheduler.Job;
import com.basketbandit.scheduler.tasks.IsAliveTask;

import java.util.concurrent.TimeUnit;

public class OneSecondlyJob extends Job {
    private final IsAliveTask isAliveTask = new IsAliveTask();

    public OneSecondlyJob() {
        super(0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(isAliveTask);
    }
}
