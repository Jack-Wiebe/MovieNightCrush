package com.filip.movienightcrush;

import com.filip.androidgames.framework.Audio;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.Sound;

import java.util.List;

import static com.filip.movienightcrush.MovieNightCrush.mMyAppsBundle;


public class MainMenuScreen extends Screen{


    private static Pixmap background;
    private static Pixmap movieNightCrushLogo;
    private static Pixmap playButton;
    private static Pixmap instructionsButton;
    private static Pixmap settingsButton;
    private static Pixmap leaderboardButton;

    private int logoXPos;
    private int logYPos;
    private int playXPos;
    private int playYPos;
    private int instructionsXPos;
    private int instructionsYPos;
    private int settingsXPos;
    private int settingsYPos;
    private int leaderboardXPos;
    private int leaderboardYPos;

    private Sound buttonClcikSound;

    public MainMenuScreen(Game game){

        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        movieNightCrushLogo = g.newPixmap("movieNightCrushLogo.png", Graphics.PixmapFormat.RGB565);
        playButton = g.newPixmap("playButton.png", Graphics.PixmapFormat.RGB565);
        instructionsButton = g.newPixmap("instructionsButton.png", Graphics.PixmapFormat.RGB565);
        settingsButton = g.newPixmap("settingsButton.png", Graphics.PixmapFormat.RGB565);
        leaderboardButton = g.newPixmap("leaderboardButton.png", Graphics.PixmapFormat.RGB565);

        logoXPos = g.getWidth() / 2 - movieNightCrushLogo.getWidth() / 2;
        playXPos = g.getWidth() / 2 - playButton.getWidth() / 2;
        instructionsXPos = g.getWidth() / 2 - instructionsButton.getWidth() / 2;
        settingsXPos = g.getWidth() / 2 - settingsButton.getWidth() / 2;
        leaderboardXPos = g.getWidth() / 2 - leaderboardButton.getWidth() / 2;

        playYPos = g.getHeight() / 2 - playButton.getHeight() / 2;
        logYPos = playYPos - 400;
        instructionsYPos = playYPos + 100;
        settingsYPos = playYPos + 200;
        leaderboardYPos = playYPos + 300;

        Audio a = game.getAudio();
        buttonClcikSound = a.newSound("Sounds/buttonClcikSound.wav");
    }

    @Override
    public void update(float deltaTime){
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++){
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP){
                if (inBounds(event, playXPos, playYPos, playButton.getWidth(), playButton.getHeight())){
                    game.setScreen(new GameScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
                if (inBounds(event, instructionsXPos, instructionsYPos, instructionsButton.getWidth(), instructionsButton.getHeight())){
                    game.setScreen(new InstructionsScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
                if (inBounds(event, settingsXPos, settingsYPos, settingsButton.getWidth(), settingsButton.getHeight())){
                    game.setScreen(new SettingScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
                if (inBounds(event, leaderboardXPos, leaderboardYPos, leaderboardButton.getWidth(), leaderboardButton.getHeight())){
                    game.setScreen(new LeaderBoardScreen(game));
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
    public void present(float deltaTime){
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 0, 0);
        g.drawPixmap(movieNightCrushLogo, logoXPos, logYPos);
        g.drawPixmap(playButton, playXPos, playYPos);
        g.drawPixmap(instructionsButton, instructionsXPos, instructionsYPos);
        g.drawPixmap(settingsButton, settingsXPos, settingsYPos);
        g.drawPixmap(leaderboardButton, leaderboardXPos, leaderboardYPos);
    }

    @Override
    public void pause(){}
    @Override
    public void resume(){}
    @Override
    public void dispose(){}
}
