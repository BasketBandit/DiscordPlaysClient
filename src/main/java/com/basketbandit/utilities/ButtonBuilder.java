package com.basketbandit.utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonBuilder {
    private final JButton button;

    public ButtonBuilder(String text) {
        this.button = new JButton(text);
        this.button.setFont(this.button.getFont().deriveFont(Font.PLAIN, 18));
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
