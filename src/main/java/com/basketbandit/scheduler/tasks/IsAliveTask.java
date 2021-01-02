package com.basketbandit.scheduler.tasks;

import com.basketbandit.DiscordPlaysClient;
import com.basketbandit.scheduler.Task;

public class IsAliveTask implements Task {
    @Override
    public void run() {
        DiscordPlaysClient.getFrames()[0].setTitle("Player: " + DiscordPlaysClient.player + " (" + DiscordPlaysClient.nickname + ") | Server: " + (!DiscordPlaysClient.clientSocket.isClosed() ? "CONNECTED" : "DISCONNECTED") + " | Controller: " + (DiscordPlaysClient.device != null && DiscordPlaysClient.device.isConnected()  ? "CONNECTED" : "DISCONNECTED"));
    }
}
