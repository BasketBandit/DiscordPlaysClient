package com.basketbandit.utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ButtonBuilder {
    private JButton button;
    Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/unifont-13.0.04.ttf")).deriveFont(Font.PLAIN, 32);

    public ButtonBuilder() throws IOException, FontFormatException {
        this.button = new JButton();
    }

    public ButtonBuilder(String text) throws IOException, FontFormatException {
        this.button = new JButton(text);
        this.button.setFont(font);
        this.button.setMargin(new Insets(-10, -10, -10, -10));
    }

    public ButtonBuilder setText(String label) {
        this.button.setText(label);
        return this;
    }

    public ButtonBuilder addActionListener(ActionListener actionListener) {
        this.button.addActionListener(actionListener);
        return this;
    }

    public ButtonBuilder setActionCommand(String command) {
        this.button.setActionCommand(command);
        return this;
    }

    public ButtonBuilder setBounds(int x, int y, int width, int height) {
        this.button.setBounds(x, y, width, height);
        return this;
    }

    public JButton build() {
        return this.button;
    }
}
