package com.basketbandit.utilities;

import java.awt.*;
import java.io.IOException;

public class Fonts {
    public static Font DEFAULT = null;

    static {
        try {
            DEFAULT = Font.createFont(Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream("/fonts/unifont-13.0.04.ttf")).deriveFont(Font.PLAIN, 32);
        } catch(FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
