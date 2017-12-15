package com.filip.androidgames.framework.impl;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Text;

public class AndroidText implements Text {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;

    public AndroidText(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public void drawText(Graphics g, Pixmap pixmap, String line, int x, int y) {
        int len = line.length();
        for(int i = 0; i < len; i++){
            char character = line.charAt(i);

            if(character == ' '){
                x += 20;
                continue;
            }

            int srcX;
            int srcWidth;
            if(character == '.'){
                srcX = 200;
                srcWidth = 10;
            }else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(pixmap, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

    @Override
    public void drawLetters(String displayText, float x, float y, int color, float textSize, String fileName) {
        Typeface typeface = Typeface.createFromAsset(assets, fileName);
        paint.setTypeface(typeface);
        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.drawText(displayText, x, y, paint);
    }

    @Override
    public void drawMultiLineLetters(String displayText, float x, float y, int color, float textSize, String fileName)
    {
        Typeface typeface = Typeface.createFromAsset(assets, fileName);
        paint.setTypeface(typeface);
        paint.setColor(color);
        paint.setTextSize(textSize);
        for (String line: displayText.split("\n"))
        {
            canvas.drawText(line, x, y, paint);
            y += -paint.ascent() + paint.descent();
        }
    }
}
