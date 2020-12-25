package com.basketbandit;

import com.basketbandit.scheduler.ScheduleHandler;
import com.basketbandit.scheduler.jobs.OneSecondlyJob;
import com.basketbandit.scheduler.jobs.TenMillisecondlyJob;
import com.basketbandit.utilities.ButtonBuilder;
import com.basketbandit.utilities.MenuItemBuilder;
import com.github.strikerx3.jxinput.XInputDevice;
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
    public static final String VERSION = "0.3.1";
    public static Socket clientSocket = new Socket();
    private static PrintWriter out;
    private static BufferedReader in;
    private String nickname = "";
    private String ip = "127.0.0.1"; // default ip
    private int port = 3197; // default port
    public static final JFrame frame = new JFrame();
    public static XInputDevice device;

    public static void main(String[] args) {
        new DiscordPlaysWSClient();
    }

    DiscordPlaysWSClient() {
        try(InputStream inputStream = new FileInputStream("./config.yaml")) {
            Map<String, String> config = new Yaml().load(inputStream);
            nickname = config.get("nickname");
            ip = config.get("ip_address");
            port = Integer.parseInt(config.get("port"));
        } catch(IOException e) {
            log.error("There was an error loading the configuration file, message: {}", e.getMessage(), e);
        }

        initXInputDevice();
        initGUI();
        startConnection(ip, port);
        ScheduleHandler.registerJob(new OneSecondlyJob());
        ScheduleHandler.registerJob(new TenMillisecondlyJob());
    }

    public void startConnection(String ip, int port) {
        try {
            log.info("Connecting to socket on address: {}", ip + ":" + port);
            clientSocket = new Socket(ip, port);
            clientSocket.setKeepAlive(true);
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            if(!sendMessage("id:" + nickname).endsWith("nickname!")) {
                sendMessage("H-hi DiscordPlaysSocketServer-senpai! u/////u");
            } else {
                clientSocket.close();
                log.warn("Client socket has closed.");
            }
        } catch(IOException e) {
            log.error("There was an error starting connection with websocket, message: {}", e.getMessage(), e);
        }
    }

    public String sendMessage(String msg) {
        try {
            if(clientSocket.isConnected()) {
                log.info("DiscordPlaysSocketClient Outputs: \"{}\"", msg);
                out.println(msg);
                String reply = in.readLine();
                log.info("DiscordPlaysSocketServer Replies: \"{}\"", reply);
                return reply;
            }
            return "";
        } catch(Exception e) {
            log.error("There was a problem sending message, message: {}", e.getMessage());
            return "";
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
                    initXInputDevice();
                }
                return;
            }
        }
        sendMessage(e.getActionCommand());
    }

    private void initXInputDevice() {
        try {
            device = XInputDevice.getDeviceFor(0);
            device.addListener(
                    new SimpleXInputDeviceListener() {
                        @Override
                        public void connected() {
                            log.info("Controller connected! :D");
                        }

                        @Override
                        public void disconnected() {
                            log.info("Controller disconnected! :(");
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
        frame.add(new ButtonBuilder("A").addActionListener(this).setActionCommand("A").setBounds(445, 170, 50, 50).build());
        frame.add(new ButtonBuilder("B").addActionListener(this).setActionCommand("B").setBounds(390, 225, 50, 50).build());
        frame.add(new ButtonBuilder("X").addActionListener(this).setActionCommand("X").setBounds(390, 115, 50, 50).build());
        frame.add(new ButtonBuilder("Y").addActionListener(this).setActionCommand("Y").setBounds(335, 170, 50, 50).build());
        frame.add(new ButtonBuilder("L").addActionListener(this).setActionCommand("LB").setBounds(60, 5, 160, 50).build());
        frame.add(new ButtonBuilder("R").addActionListener(this).setActionCommand("RB").setBounds(335, 5, 160, 50).build());

        frame.add(new ButtonBuilder("B!").addActionListener(this).setActionCommand("BH").setBounds(445, 225, 50, 50).build());

        frame.add(new ButtonBuilder("➡").addActionListener(this).setActionCommand("R").setBounds(170, 170, 50, 50).build());
        frame.add(new ButtonBuilder("⬇").addActionListener(this).setActionCommand("D").setBounds(115, 225, 50, 50).build());
        frame.add(new ButtonBuilder("⬆").addActionListener(this).setActionCommand("U").setBounds(115, 115, 50, 50).build());
        frame.add(new ButtonBuilder("⬅").addActionListener(this).setActionCommand("L").setBounds(60, 170, 50, 50).build());
        frame.add(new ButtonBuilder("↗").addActionListener(this).setActionCommand("UR").setBounds(170, 115, 50, 50).build());
        frame.add(new ButtonBuilder("↘").addActionListener(this).setActionCommand("DR").setBounds(170, 225, 50, 50).build());
        frame.add(new ButtonBuilder("↖").addActionListener(this).setActionCommand("UL").setBounds(60, 115, 50, 50).build());
        frame.add(new ButtonBuilder("↙").addActionListener(this).setActionCommand("DL").setBounds(60, 225, 50, 50).build());

        frame.add(new ButtonBuilder("⏩").addActionListener(this).setActionCommand("R1").setBounds(225, 170, 50, 50).build());
        frame.add(new ButtonBuilder("⏬").addActionListener(this).setActionCommand("D1").setBounds(115, 280, 50, 50).build());
        frame.add(new ButtonBuilder("⏫").addActionListener(this).setActionCommand("U1").setBounds(115, 60, 50, 50).build());
        frame.add(new ButtonBuilder("⏪").addActionListener(this).setActionCommand("L1").setBounds(5, 170, 50, 50).build());

        frame.add(new ButtonBuilder("START").addActionListener(this).setActionCommand("P").setBounds(115, 335, 160, 50).build());
        frame.add(new ButtonBuilder("SELECT").addActionListener(this).setActionCommand("S").setBounds(280, 335, 160, 50).build());

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menu.add(new MenuItemBuilder("Connect").addActionListener(this).setActionCommand("connect").build());
        menu.add(new MenuItemBuilder("Disconnect").addActionListener(this).setActionCommand("disconnect").build());
        menu.add(new MenuItemBuilder("Connect (Controller)").addActionListener(this).setActionCommand("connect_controller").build());
        menu.addSeparator();
        menu.add("Version ~ " + VERSION);
        bar.add(menu);

        frame.setJMenuBar(bar);
        frame.setSize(560, 450);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
