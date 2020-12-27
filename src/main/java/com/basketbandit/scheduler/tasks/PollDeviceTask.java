package com.basketbandit.scheduler.tasks;

import com.basketbandit.DiscordPlaysWSClient;
import com.basketbandit.scheduler.Task;

public class PollDeviceTask implements Task {
    @Override
    public void run() {
        if(DiscordPlaysWSClient.device != null) {
            DiscordPlaysWSClient.device.poll();
        }
    }
}
