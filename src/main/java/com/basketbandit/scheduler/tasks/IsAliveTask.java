package com.basketbandit.scheduler.tasks;

import com.basketbandit.DiscordPlaysWSClient;
import com.basketbandit.scheduler.Task;

public class IsAliveTask implements Task {

    @Override
    public void run() {
        DiscordPlaysWSClient.statusLabel.setText("Status: " + (DiscordPlaysWSClient.clientSocket.isConnected() ? "CONNECTED" : "DISCONNECTED"));
    }
}
