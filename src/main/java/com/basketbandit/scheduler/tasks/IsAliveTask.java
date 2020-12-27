package com.basketbandit.scheduler.tasks;

import com.basketbandit.DiscordPlaysWSClient;
import com.basketbandit.scheduler.Task;

public class IsAliveTask implements Task {
    @Override
    public void run() {
        DiscordPlaysWSClient.frame.setTitle("Player: " + DiscordPlaysWSClient.player + " | Status: " + (!DiscordPlaysWSClient.clientSocket.isClosed() ? "CONNECTED" : "DISCONNECTED") + " | Controller: " + (DiscordPlaysWSClient.device != null && DiscordPlaysWSClient.device.isConnected()  ? "CONNECTED" : "DISCONNECTED"));
    }
}
