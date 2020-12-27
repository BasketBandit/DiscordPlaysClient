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
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DiscordPlaysWSClient implements ActionListener {
    private static final Logger log = LoggerFactory.getLogger(DiscordPlaysWSClient.class);
    public static final String VERSION = "0.4.0";
    public static Socket clientSocket = new Socket();
    private static PrintWriter out;
    private static BufferedReader in;
    private static String nickname = "";
    private static String ip = "127.0.0.1"; // default ip
    public static int player = 1;
    private static int port = 3197; // default port
    public static XInputDevice device;

    public static final JFrame frame = new JFrame();
    private static JMenu playerMenu;

    public static void main(String[] args) {
        new DiscordPlaysWSClient();
    }

    DiscordPlaysWSClient() {
        try(InputStream inputStream = new FileInputStream("./config.yaml")) {
            Map<String, Object> config = new Yaml().load(inputStream);
            nickname = (String)config.get("nickname");
            ip = (String)config.get("ip_address");
            port = (int)config.get("port");
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
            clientSocket = new Socket(ip, port);
            clientSocket.setKeepAlive(true);
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            log.info("Connected to socket on address: {}", ip + ":" + port);
            if(!sendMessage("@" + nickname).endsWith("nickname!")) {
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

    public void sendCommand(String cmd) {
        if(cmd.startsWith("STATE")) {
            sendMessage(cmd);
        } else {
            sendMessage(player + "#" + cmd); // prepend play number to input commands
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
        final String command = e.getActionCommand();
        switch(command) {
            case "CONNECT" : {
                if(clientSocket.isClosed()) {
                    startConnection(ip, port);
                }
                return;
            }
            case "DISCONNECT" : {
                if(!clientSocket.isClosed()) {
                    stopConnection();
                }
                return;
            }
            case "CONTROLLER_CONNECT" : {
                if(device == null || !device.isConnected()) {
                    initXInputDevice();
                }
                return;
            }
            case "CONTROLLER_DISCONNECT" : {
                device = null;
                return;
            }
        }

        if(command.startsWith("PLAYER_SWITCH")) {
            String[] params = command.split("#");
            playerMenu.getItem(player-1).setText("Player " + player);
            player = Integer.parseInt(params[1]);
            playerMenu.getItem(player-1).setText("Player " + player + " <<");
            log.info("Controller switched to player {}.", player);
            return;
        }

        sendCommand(command);
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
                            sendCommand((button + "_" + pressed).toUpperCase());
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


        JMenu socketMenu = new JMenu("Socket");
        socketMenu.add(new MenuItemBuilder("Connect").addActionListener(this).setActionCommand("CONNECT").build());
        socketMenu.add(new MenuItemBuilder("Disconnect").addActionListener(this).setActionCommand("DISCONNECT").build());
        socketMenu.addSeparator();
        socketMenu.add("Version ~ " + VERSION);

        JMenu controllerMenu = new JMenu("Controller");
        controllerMenu.add(new MenuItemBuilder("Connect").addActionListener(this).setActionCommand("CONTROLLER_CONNECT").build());
        controllerMenu.add(new MenuItemBuilder("Disconnect").addActionListener(this).setActionCommand("CONTROLLER_DISCONNECT").build());

        playerMenu = new JMenu("Player");
        playerMenu.add(new MenuItemBuilder("Player 1 <<").addActionListener(this).setActionCommand("PLAYER_SWITCH#1").build());
        playerMenu.add(new MenuItemBuilder("Player 2").addActionListener(this).setActionCommand("PLAYER_SWITCH#2").build());
        playerMenu.add(new MenuItemBuilder("Player 3").addActionListener(this).setActionCommand("PLAYER_SWITCH#3").build());
        playerMenu.add(new MenuItemBuilder("Player 4").addActionListener(this).setActionCommand("PLAYER_SWITCH#4").build());

        JMenu stateMenu = new JMenu("States");
        stateMenu.add("SAVE");
        stateMenu.add(new MenuItemBuilder("Slot 1").addActionListener(this).setActionCommand("STATE#S1").build());
        stateMenu.add(new MenuItemBuilder("Slot 2").addActionListener(this).setActionCommand("STATE#S2").build());
        stateMenu.add(new MenuItemBuilder("Slot 3").addActionListener(this).setActionCommand("STATE#S3").build());
        stateMenu.addSeparator();
        stateMenu.add("LOAD");
        stateMenu.add(new MenuItemBuilder("Slot 1").addActionListener(this).setActionCommand("STATE#L1").build());
        stateMenu.add(new MenuItemBuilder("Slot 2").addActionListener(this).setActionCommand("STATE#L2").build());
        stateMenu.add(new MenuItemBuilder("Slot 3").addActionListener(this).setActionCommand("STATE#L3").build());

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(socketMenu);
        menuBar.add(controllerMenu);
        menuBar.add(playerMenu);
        menuBar.add(stateMenu);
        frame.setJMenuBar(menuBar);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopConnection();
                System.exit(0);
            }
        });


        frame.setSize(560, 450);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
