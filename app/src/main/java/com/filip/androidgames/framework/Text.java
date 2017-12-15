package com.filip.androidgames.framework;

public interface Text {
    public void drawText(Graphics g, Pixmap pixmap, String line, int x, int y);
    public void drawLetters(String displayText, float x, float y, int color, float textSize, String fileName);
    public void drawMultiLineLetters(String displayText, float x, float y, int color, float textSize, String fileName);
}
