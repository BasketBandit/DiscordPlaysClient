package com.basketbandit.scheduler.jobs;

import com.basketbandit.scheduler.Job;
import com.basketbandit.scheduler.tasks.PollDeviceTask;

import java.util.concurrent.TimeUnit;

public class FiftyMillisecondlyJob extends Job {
    private final PollDeviceTask pollDeviceTask = new PollDeviceTask();

    public FiftyMillisecondlyJob() {
        super(0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        handleTask(pollDeviceTask);
    }
}
