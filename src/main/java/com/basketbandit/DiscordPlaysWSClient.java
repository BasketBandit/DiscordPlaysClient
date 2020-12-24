package com.basketbandit;

import com.basketbandit.scheduler.ScheduleHandler;
import com.basketbandit.scheduler.jobs.FiveSecondlyJob;
import com.basketbandit.scheduler.jobs.TenMillisecondlyJob;
import com.basketbandit.utilities.ButtonBuilder;
import com.basketbandit.utilities.MenuItemBuilder;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.XInputDevice14;
import com.github.strikerx3.jxinput.enums.XInputButton;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;
import com.github.strikerx3.jxinput.listener.SimpleXInputDeviceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DiscordPlaysWSClient implements ActionListener {
    private static final Logger log = LoggerFactory.getLogger(DiscordPlaysWSClient.class);
    public static final String VERSION = "0.3.0";
    public static Socket clientSocket = new Socket();
    private static PrintWriter out;
    private static BufferedReader in;
    private String ip = "127.0.0.1";
    private int port = 0;
    public static final JFrame f = new JFrame();
    public static XInputDevice device;

    public static void main(String[] args) {
        new DiscordPlaysWSClient();
    }

    DiscordPlaysWSClient() {
        try(InputStream inputStream = new FileInputStream("./config.yaml")) {
            Map<String, String> config = new Yaml().load(inputStream);
            ip = config.get("ip_address");
            port = Integer.parseInt(config.get("port"));
        } catch(IOException e) {
            log.error("There was an error loading the configuration file, message: {}", e.getMessage(), e);
        }
        initGUI();
        initController();
        startConnection(ip, port);
        ScheduleHandler.registerJob(new FiveSecondlyJob());
        ScheduleHandler.registerJob(new TenMillisecondlyJob());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "connect" : {
                if(clientSocket.isClosed()) {
                    startConnection(ip, port);
                }
                return;
            }
            case "disconnect" : {
                if(!clientSocket.isClosed()) {
                    stopConnection();
                }
                return;
            }
            case "connect_controller" : {
                if(!device.isConnected()) {
                    initController();
                }
                return;
            }
        }
        sendMessage(e.getActionCommand());
    }

    public void startConnection(String ip, int port) {
        try {
            log.info("Connecting to socket on address: {}", ip + ":" + port);
            clientSocket = new Socket(ip, port);
            clientSocket.setKeepAlive(true);
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            sendMessage("H-hi DiscordPlaysSocketServer-senpai! u/////u");
        } catch(IOException e) {
            log.error("There was an error starting connection with websocket, message: {}", e.getMessage(), e);
        }
    }

    public void sendMessage(String msg) {
        try {
            log.info("DiscordPlaysSocketClient Outputs: \"{}\"", msg);
            out.println(msg);
            log.info("DiscordPlaysSocketServer Replies: \"{}\"", in.readLine());
        } catch(Exception e) {
            log.error("There was a problem sending message, message: {}", e.getMessage(), e);
        }
    }

    public void stopConnection() {
        try {
            sendMessage(".");
            in.close();
            out.close();
            clientSocket.close();
        } catch(Exception e) {
            log.error("There was a problem stopping socket connection, message: {}", e.getMessage(), e);
        }
    }

    private void initController() {
        try {
            device = XInputDevice.getDeviceFor(0);
            XInputDevice14.setEnabled(true);
            device.addListener(
                    new SimpleXInputDeviceListener() {
                        @Override
                        public void connected() {
                            log.info("Controller detected and connected! :D");
                        }

                        @Override
                        public void disconnected() {
                            log.info("Controller lost and disconnected! :(");
                        }

                        @Override
                        public void buttonChanged(final XInputButton button, final boolean pressed) {
                            sendMessage((button + "_" + pressed).toUpperCase());
                        }
                    });
        } catch(XInputNotLoadedException e) {
            log.error("There was an error when initialising controller, message: {}", e.getMessage(), e);
        }
    }

    private void initGUI() {
        f.add(new ButtonBuilder("A").addActionListener(this).setActionCommand("🇦").setBounds(445, 195, 50, 50).build());
        f.add(new ButtonBuilder("B").addActionListener(this).setActionCommand("🇧").setBounds(390, 250, 50, 50).build());
        f.add(new ButtonBuilder("X").addActionListener(this).setActionCommand("🇽").setBounds(390, 140, 50, 50).build());
        f.add(new ButtonBuilder("Y").addActionListener(this).setActionCommand("🇾").setBounds(335, 195, 50, 50).build());
        f.add(new ButtonBuilder("L").addActionListener(this).setActionCommand("🇱").setBounds(60, 20, 150, 50).build());
        f.add(new ButtonBuilder("R").addActionListener(this).setActionCommand("🇷").setBounds(335, 20, 150, 50).build());

        f.add(new ButtonBuilder("B!").addActionListener(this).setActionCommand("🅱️").setBounds(445, 250, 50, 50).build());

        f.add(new ButtonBuilder("➡").addActionListener(this).setActionCommand("➡️").setBounds(170, 195, 50, 50).build());
        f.add(new ButtonBuilder("⬇").addActionListener(this).setActionCommand("⬇️").setBounds(115, 250, 50, 50).build());
        f.add(new ButtonBuilder("⬆").addActionListener(this).setActionCommand("⬆️").setBounds(115, 140, 50, 50).build());
        f.add(new ButtonBuilder("⬅").addActionListener(this).setActionCommand("⬅️").setBounds(60, 195, 50, 50).build());
        f.add(new ButtonBuilder("↗").addActionListener(this).setActionCommand("↗️").setBounds(170, 140, 50, 50).build());
        f.add(new ButtonBuilder("↘").addActionListener(this).setActionCommand("↘️").setBounds(170, 250, 50, 50).build());
        f.add(new ButtonBuilder("↖").addActionListener(this).setActionCommand("↖️").setBounds(60, 140, 50, 50).build());
        f.add(new ButtonBuilder("↙").addActionListener(this).setActionCommand("↙️").setBounds(60, 250, 50, 50).build());

        f.add(new ButtonBuilder("⏩").addActionListener(this).setActionCommand("⏩").setBounds(225, 195, 50, 50).build());
        f.add(new ButtonBuilder("⏬").addActionListener(this).setActionCommand("⏬").setBounds(115, 305, 50, 50).build());
        f.add(new ButtonBuilder("⏫").addActionListener(this).setActionCommand("⏫").setBounds(115, 85, 50, 50).build());
        f.add(new ButtonBuilder("⏪").addActionListener(this).setActionCommand("⏪").setBounds(5, 195, 50, 50).build());

        f.add(new ButtonBuilder("START").addActionListener(this).setActionCommand("⏸️").setBounds(115, 365, 150, 50).build());
        f.add(new ButtonBuilder("SELECT").addActionListener(this).setActionCommand("⏯️").setBounds(280, 365, 150, 50).build());

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.add(new MenuItemBuilder("Socket Connect").addActionListener(this).setActionCommand("connect").build());
        menu.add(new MenuItemBuilder("Socket Disconnect").addActionListener(this).setActionCommand("disconnect").build());
        menu.add(new MenuItemBuilder("Controller Connect").addActionListener(this).setActionCommand("connect_controller").build());
        bar.add(menu);

        f.setJMenuBar(bar);
        f.setSize(600, 485);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.setVisible(true);
    }
}
