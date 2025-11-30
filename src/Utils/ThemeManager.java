package Utils;

import java.awt.Color;

public class ThemeManager {
    public enum Theme {
        LIGHT, DARK
    }
    
    private static Theme currentTheme = Theme.LIGHT;
    
    // Light theme colors (current orange/cream scheme)
    public static final Color LIGHT_BG = new Color(255, 248, 240);
    public static final Color LIGHT_PRIMARY = new Color(255, 107, 53); // Warm orange
    public static final Color LIGHT_SECONDARY = new Color(76, 175, 80); // Green
    public static final Color LIGHT_ACCENT = new Color(255, 240, 230);
    public static final Color LIGHT_TEXT = new Color(60, 60, 60);
    public static final Color LIGHT_TEXT_LIGHT = new Color(120, 120, 120);
    public static final Color LIGHT_BORDER = new Color(255, 200, 180);
    public static final Color LIGHT_PANEL = Color.WHITE;
    public static final Color LIGHT_RED = new Color(220, 53, 69);
    public static final Color LIGHT_BLUE = new Color(100, 149, 237);
    
    // Dark theme colors (purple/pink/blue palette)
    public static final Color DARK_BG = new Color(18, 18, 30); // Almost black
    public static final Color DARK_PRIMARY = new Color(138, 43, 226); // Purple
    public static final Color DARK_SECONDARY = new Color(255, 20, 147); // Pink
    public static final Color DARK_ACCENT = new Color(70, 130, 180); // Blue
    public static final Color DARK_TEXT = new Color(240, 240, 240); // Light gray
    public static final Color DARK_TEXT_LIGHT = new Color(180, 180, 180);
    public static final Color DARK_BORDER = new Color(138, 43, 226);
    public static final Color DARK_PANEL = new Color(30, 30, 45);
    public static final Color DARK_RED = new Color(255, 99, 132);
    public static final Color DARK_GREEN = new Color(50, 205, 50);
    
    public static Theme getCurrentTheme() {
        return currentTheme;
    }
    
    public static void setTheme(Theme theme) {
        currentTheme = theme;
    }
    
    public static void toggleTheme() {
        currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
    }
    
    // Get colors based on current theme
    public static Color getBackground() {
        return currentTheme == Theme.LIGHT ? LIGHT_BG : DARK_BG;
    }
    
    public static Color getPrimary() {
        return currentTheme == Theme.LIGHT ? LIGHT_PRIMARY : DARK_PRIMARY;
    }
    
    public static Color getSecondary() {
        return currentTheme == Theme.LIGHT ? LIGHT_SECONDARY : DARK_GREEN;
    }
    
    public static Color getAccent() {
        return currentTheme == Theme.LIGHT ? LIGHT_ACCENT : new Color(40, 40, 60);
    }
    
    public static Color getText() {
        return currentTheme == Theme.LIGHT ? LIGHT_TEXT : DARK_TEXT;
    }
    
    public static Color getTextLight() {
        return currentTheme == Theme.LIGHT ? LIGHT_TEXT_LIGHT : DARK_TEXT_LIGHT;
    }
    
    public static Color getBorder() {
        return currentTheme == Theme.LIGHT ? LIGHT_BORDER : DARK_BORDER;
    }
    
    public static Color getPanel() {
        return currentTheme == Theme.LIGHT ? LIGHT_PANEL : DARK_PANEL;
    }
    
    public static Color getRed() {
        return currentTheme == Theme.LIGHT ? LIGHT_RED : DARK_RED;
    }
    
    public static Color getBlue() {
        return currentTheme == Theme.LIGHT ? LIGHT_BLUE : DARK_ACCENT;
    }
}

