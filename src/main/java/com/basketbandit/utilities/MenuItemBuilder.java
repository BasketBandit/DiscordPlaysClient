package com.basketbandit.utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuItemBuilder {
    private final JMenuItem item;

    public MenuItemBuilder(String text) {
        this.item = new JMenuItem(text);
        this.item.setFont(this.item.getFont().deriveFont(Font.PLAIN, 18));
    }

    public MenuItemBuilder setText(String label) {
        this.item.setText(label);
        return this;
    }

    public MenuItemBuilder addActionListener(ActionListener actionListener) {
        this.item.addActionListener(actionListener);
        return this;
    }

    public MenuItemBuilder setActionCommand(String command) {
        this.item.setActionCommand(command);
        return this;
    }

    public JMenuItem build() {
            return this.item;
        }
}
