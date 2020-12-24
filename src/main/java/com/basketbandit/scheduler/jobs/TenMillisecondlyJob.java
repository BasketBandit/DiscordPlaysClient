package com.basketbandit.scheduler.jobs;

import com.basketbandit.scheduler.Job;
import com.basketbandit.scheduler.tasks.PollDeviceTask;

import java.util.concurrent.TimeUnit;

public class TenMillisecondlyJob extends Job {
    private final PollDeviceTask pollDeviceTask = new PollDeviceTask();

    public TenMillisecondlyJob() {
        super(0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        handleTask(pollDeviceTask);
    }
}
