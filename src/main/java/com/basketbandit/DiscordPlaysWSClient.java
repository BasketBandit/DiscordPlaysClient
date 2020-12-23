package com.basketbandit;

import com.basketbandit.utilities.ButtonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DiscordPlaysWSClient implements ActionListener {
    private static final Logger log = LoggerFactory.getLogger(DiscordPlaysWSClient.class);
    private static final String VERSION = "0.1.0";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws IOException, FontFormatException {
       new DiscordPlaysWSClient().startConnection(args[0], Integer.parseInt(args[1]));
    }

    DiscordPlaysWSClient() throws IOException, FontFormatException {
        JFrame f = new JFrame();
        f.setTitle("DiscordPlaysWSClient - " + VERSION);
        f.setBackground(Color.DARK_GRAY);

        f.add(new ButtonBuilder("A").addActionListener(this).setActionCommand("🇦").setBounds(420,200,50,50).build());
        f.add(new ButtonBuilder("B").addActionListener(this).setActionCommand("🇧").setBounds(365,260,50,50).build());
        f.add(new ButtonBuilder("X").addActionListener(this).setActionCommand("🇽").setBounds(365,140,50,50).build());
        f.add(new ButtonBuilder("Y").addActionListener(this).setActionCommand("🇾").setBounds(310,200,50,50).build());
        f.add(new ButtonBuilder("L").addActionListener(this).setActionCommand("🇱").setBounds(30,20,100,50).build());
        f.add(new ButtonBuilder("R").addActionListener(this).setActionCommand("🇷").setBounds(365,20,100,50).build());

        f.add(new ButtonBuilder("B!").addActionListener(this).setActionCommand("🅱️").setBounds(420,260,50,50).build());

        f.add(new ButtonBuilder("➡").addActionListener(this).setActionCommand("➡️").setBounds(190,200,50,50).build());
        f.add(new ButtonBuilder("⬇").addActionListener(this).setActionCommand("⬇️").setBounds(135,260,50,50).build());
        f.add(new ButtonBuilder("⬆").addActionListener(this).setActionCommand("⬆️").setBounds(135,140,50,50).build());
        f.add(new ButtonBuilder("⬅").addActionListener(this).setActionCommand("⬅️").setBounds(80,200,50,50).build());
        f.add(new ButtonBuilder("↗").addActionListener(this).setActionCommand("↗️").setBounds(190,140,50,50).build());
        f.add(new ButtonBuilder("↘").addActionListener(this).setActionCommand("↘️").setBounds(190,260,50,50).build());
        f.add(new ButtonBuilder("↖").addActionListener(this).setActionCommand("↖️").setBounds(80,140,50,50).build());
        f.add(new ButtonBuilder("↙").addActionListener(this).setActionCommand("↙️").setBounds(80,260,50,50).build());

        f.add(new ButtonBuilder("⏩").addActionListener(this).setActionCommand("⏩").setBounds(245,200,50,50).build());
        f.add(new ButtonBuilder("⏬").addActionListener(this).setActionCommand("⏬").setBounds(135,315,50,50).build());
        f.add(new ButtonBuilder("⏫").addActionListener(this).setActionCommand("⏫").setBounds(135,85,50,50).build());
        f.add(new ButtonBuilder("⏪").addActionListener(this).setActionCommand("⏪").setBounds(25,200,50,50).build());

        f.add(new ButtonBuilder("START").addActionListener(this).setActionCommand("⏸️").setBounds(75,400,150,50).build());
        f.add(new ButtonBuilder("SELECT").addActionListener(this).setActionCommand("⏯️").setBounds(235,400,150,50).build());

        f.add(new ButtonBuilder("Exit").addActionListener(this).setActionCommand(".").setBounds(415,400,75,50).build());

        f.setSize(500,500);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.setVisible(true);
    }

    void a() {
      //  🔻
      //  ⌛
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sendMessage(e.getActionCommand());
    }

    public void startConnection(String ip, int port) {
        try {
            log.info("Connecting to websocket on address: {}", ip + ":" + port);
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            sendMessage("Hello DiscordPlays!");
            // Sending . ends connection log.info(sendMessage("."));
        } catch(IOException e) {
            log.error("There was an error starting connection with websocket, message: {}", e.getMessage(), e);
        }
    }

    public String sendMessage(String msg) {
        try {
            log.info("Sending message to socket server: \"{}\"", msg);
            out.println(msg);
            return in.readLine();
        } catch(Exception e) {
            log.error("There was a problem sending message: {}", e.getMessage(), e);
            return "";
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch(Exception e) {
            log.error("There was a problem stopping socket connection, message: {}", e.getMessage(), e);
        }
    }
}
