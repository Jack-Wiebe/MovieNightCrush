package com.filip.movienightcrush;

import com.filip.androidgames.framework.Audio;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.Sound;

import java.io.FileInputStream;
import java.util.List;

import static com.filip.movienightcrush.MovieNightCrush.mMyAppsBundle;

public class LeaderBoardScreen extends Screen {
    private static Pixmap background;
    public static Pixmap backButton;

    private int backXPos;
    private int backYPos;

    private Sound buttonClcikSound;

    public LeaderBoardScreen(Game game){
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("leaderBoardBackground.png", Graphics.PixmapFormat.RGB565);
        backButton = g.newPixmap("backButton.png", Graphics.PixmapFormat.RGB565);

        backXPos = g.getWidth() / 2 - backButton.getWidth() / 2;
        backYPos = g.getHeight() - backButton.getHeight() - 100;

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
                if (inBounds(event, backXPos, backYPos, backButton.getWidth(), backButton.getHeight())){
                    game.setScreen(new MainMenuScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime)
    {
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        g.drawPixmap(backButton, backXPos, backYPos);

        game.showLeaderboard();
    }

    @Override
    public void pause(){}

    @Override
    public void resume(){}

    @Override
    public void dispose(){
    }
}
