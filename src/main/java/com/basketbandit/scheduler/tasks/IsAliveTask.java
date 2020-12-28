package com.basketbandit.scheduler.tasks;

import com.basketbandit.DiscordPlaysWSClient;
import com.basketbandit.scheduler.Task;

public class IsAliveTask implements Task {
    @Override
    public void run() {
        DiscordPlaysWSClient.getFrames()[0].setTitle("Player: " + DiscordPlaysWSClient.player + " (" + DiscordPlaysWSClient.nickname + ") | Server: " + (!DiscordPlaysWSClient.clientSocket.isClosed() ? "CONNECTED" : "DISCONNECTED") + " | Controller: " + (DiscordPlaysWSClient.device != null && DiscordPlaysWSClient.device.isConnected()  ? "CONNECTED" : "DISCONNECTED"));
    }
}
