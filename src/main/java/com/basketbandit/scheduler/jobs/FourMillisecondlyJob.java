package com.basketbandit.scheduler.jobs;

import com.basketbandit.scheduler.Job;
import com.basketbandit.scheduler.tasks.PollDeviceTask;

import java.util.concurrent.TimeUnit;

public class FourMillisecondlyJob extends Job {
    private final PollDeviceTask pollDeviceTask = new PollDeviceTask();

    public FourMillisecondlyJob() {
        super(0, 4, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        handleTask(pollDeviceTask);
    }
}
