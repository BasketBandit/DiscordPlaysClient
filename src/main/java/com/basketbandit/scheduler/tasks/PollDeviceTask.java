package com.basketbandit.scheduler.tasks;

import com.basketbandit.DiscordPlaysClient;
import com.basketbandit.scheduler.Task;

public class PollDeviceTask implements Task {
    @Override
    public void run() {
        if(DiscordPlaysClient.device != null) {
            DiscordPlaysClient.device.poll();
        }
    }
}
