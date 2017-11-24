package com.filip.movienightcrush;

import android.graphics.Color;

import com.filip.androidgames.framework.Audio;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.Sound;
import com.filip.androidgames.framework.Text;

import java.util.List;

import static com.filip.movienightcrush.MovieNightCrush.mMyAppsBundle;

public class OptionsScreen extends Screen {
    private static Pixmap background;
    public static Pixmap quitButton;
    public static Pixmap mainMenuButton;
    public static Pixmap musicOnIconButton;
    public static Pixmap musicOffIconButton;
    public static Pixmap soundEffectOnIcon;
    public static Pixmap soundEffectOffIcon;

    private int quitXPos;
    private int quitYPos;
    private int mainMenuXPos;
    private int mainMenuYPos;
    private int musicOnIconXPos;
    private int musicOnIconYPos;
    private int soundEffectOnIconXPos;
    private int soundEffectOnIconYPos;

    private Sound buttonClcikSound;

    public OptionsScreen(Game game){
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("optionsBackground.png", Graphics.PixmapFormat.RGB565);
        quitButton = g.newPixmap("backButton.png", Graphics.PixmapFormat.RGB565);
        mainMenuButton = g.newPixmap("mainMenuButton.png", Graphics.PixmapFormat.RGB565);
        musicOnIconButton = g.newPixmap("musicOnIcon.png", Graphics.PixmapFormat.RGB565);
        musicOffIconButton = g.newPixmap("musicOffIcon.png", Graphics.PixmapFormat.RGB565);
        soundEffectOnIcon = g.newPixmap("soundEffectOnIcon.png", Graphics.PixmapFormat.RGB565);
        soundEffectOffIcon = g.newPixmap("soundEffectOffIcon.png", Graphics.PixmapFormat.RGB565);

        quitXPos = g.getWidth() / 2 - quitButton.getWidth() / 2;
        quitYPos = g.getHeight() - quitButton.getHeight() - 100;
        mainMenuXPos = g.getWidth() / 2 - mainMenuButton.getWidth() / 2;
        mainMenuYPos = quitYPos - 100;
        musicOnIconXPos = g.getWidth() - musicOnIconButton.getWidth() - 100;
        musicOnIconYPos = g.getHeight() / 3;
        soundEffectOnIconXPos = musicOnIconXPos;
        soundEffectOnIconYPos = musicOnIconYPos + 200;

        Audio a = game.getAudio();
        buttonClcikSound = a.newSound("Sounds/buttonClcikSound.wav");
    }

    @Override
    public void update(float deltaTime){
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if(event.type == Input.TouchEvent.TOUCH_UP){
                if (inBounds(event, quitXPos, quitYPos, quitButton.getWidth(), quitButton.getHeight())){
                    game.setScreen(new GameScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
                if (inBounds(event, mainMenuXPos, mainMenuYPos, mainMenuButton.getWidth(), mainMenuButton.getHeight())){
                    game.setScreen(new MainMenuScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
                if (inBounds(event, musicOnIconXPos, musicOnIconYPos, musicOnIconButton.getWidth(), musicOnIconButton.getHeight())){
                    if(mMyAppsBundle.getInt("musicOn") == 1)
                    {
                        mMyAppsBundle.putInt("musicOn",0);
                    }
                    else
                    {
                        mMyAppsBundle.putInt("musicOn",1);
                    }
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);

                    }
                    return;
                }
                if (inBounds(event, soundEffectOnIconXPos, soundEffectOnIconYPos, soundEffectOnIcon.getWidth(), soundEffectOnIcon.getHeight())){
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                        mMyAppsBundle.putInt("soundEffectOn",0);
                    }
                    else
                    {
                        mMyAppsBundle.putInt("soundEffectOn",1);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime){
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        g.drawPixmap(quitButton, quitXPos, quitYPos);
        g.drawPixmap(mainMenuButton, mainMenuXPos, mainMenuYPos);

        if(mMyAppsBundle.getInt("musicOn") == 1)
        {
            g.drawPixmap(musicOnIconButton, musicOnIconXPos, musicOnIconYPos);
        }
        else
        {
            g.drawPixmap(musicOffIconButton, musicOnIconXPos, musicOnIconYPos);
        }
        if(mMyAppsBundle.getInt("soundEffectOn") == 1)
        {
            g.drawPixmap(soundEffectOnIcon, soundEffectOnIconXPos, soundEffectOnIconYPos);
        }
        else
        {
            g.drawPixmap(soundEffectOffIcon, soundEffectOnIconXPos, soundEffectOnIconYPos);
        }

        Text text = game.getText();
        String musicText =   "Music";
        String soundEffectText =   "Sound Effect";
        float musicTextX = (float)musicOnIconXPos - 500.f;
        float musicTextY = (float)musicOnIconYPos + musicOnIconButton.getHeight() / 2;
        float soundEffectTextX = musicTextX;
        float soundEffectTextY = musicTextY + 200.f;
        int textColor = Color.BLACK;
        float textSize = 40.0f;
        String textFileName = "Capture_it.ttf";
        text.drawMultiLineLetters(musicText, musicTextX, musicTextY, textColor, textSize, textFileName);
        text.drawMultiLineLetters(soundEffectText, soundEffectTextX, soundEffectTextY, textColor, textSize, textFileName);
    }

    @Override
    public void pause(){}

    @Override
    public void resume(){}

    @Override
    public void dispose(){}
}
