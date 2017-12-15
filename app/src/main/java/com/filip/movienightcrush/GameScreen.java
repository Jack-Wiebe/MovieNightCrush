package com.filip.movienightcrush;

import android.graphics.Color;

import com.filip.androidgames.framework.Audio;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input.TouchEvent;
import com.filip.androidgames.framework.Music;
import com.filip.androidgames.framework.Pixmap;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.Sound;
import com.filip.androidgames.framework.Text;

import java.io.FileInputStream;
import java.util.List;

import static com.filip.movienightcrush.MovieNightCrush.mMyAppsBundle;

public class GameScreen extends Screen {
    private static Pixmap background;
    public static Pixmap optionsButton;
    public static Pixmap gameOverImage;
    public static Pixmap gameLevelCompleteImage;

    private int optionXPos;
    private int optionYPos;
    private int adViewHeight = 200;
    private int gameOverXPos;
    private int gameOverYPos;

    private Sound buttonClcikSound;
    private Sound winLevelSound;
    private Sound loseLevelSound;
    private Music backgroundMusic;
    private int playSoundCount = 0;

    private Grid gameGrid;
    private int gameGridXPos;
    private int gameGridYPos;

    private int touchTimes = 0;
    private int firstTouchX;
    private int firstTouchY;
    private int secondTouchX;
    private int secondTouchY;

    private Text text = game.getText();
    private float textY = 120.0f;
    private int textColor = Color.GRAY;
    private float textSize_Big = 40.0f;
    private float textSize = 30.0f;
    private String textFileName = "Capture_it.ttf";

    private boolean isGameLevelComplete;

    public GameScreen(Game game){
        super(game);
        Graphics g = game.getGraphics();
        background = g.newPixmap("gameBackground.png", Graphics.PixmapFormat.RGB565);
        optionsButton = g.newPixmap("optionsButton.png", Graphics.PixmapFormat.RGB565);
        gameOverImage = g.newPixmap("gameOver.png", Graphics.PixmapFormat.RGB565);
        gameLevelCompleteImage = g.newPixmap("gameLevelComplete.png", Graphics.PixmapFormat.RGB565);

        optionXPos = g.getWidth() / 2 - optionsButton.getWidth() / 2;
        optionYPos = g.getHeight() - optionsButton.getHeight() - adViewHeight;
        gameOverXPos = g.getWidth() / 2 - gameOverImage.getWidth() / 2;
        gameOverYPos = g.getHeight() / 2 - gameOverImage.getHeight() / 2;

        Audio a = game.getAudio();
        buttonClcikSound = a.newSound("Sounds/buttonClcikSound.wav");
        winLevelSound = a.newSound("Sounds/winLevelSound.wav");
        loseLevelSound = a.newSound("Sounds/loseLevelSound.wav");

        backgroundMusic = a.newMusic("Sounds/backgroundMusic.mp3");
        if(mMyAppsBundle.getInt("musicOn") == 1)
        {
            backgroundMusic.play();
            backgroundMusic.setVolume(0.3f);//30% volume
        }

        gameGrid = new Grid(game,g);
        gameGridXPos = gameGrid.SCREENWIDTHOFFSET;
        gameGridYPos = gameGrid.SCREENHEIGHTOFFSET;

//        game.unlockAchievementOne();
    }

    @Override
    public void update(float deltaTime){
        gameGrid.checkMatches();

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP){
                if (inBounds(event, optionXPos, optionYPos, optionsButton.getWidth(), optionsButton.getHeight())){
                    game.setScreen(new OptionsScreen(game));
                    if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                    {
                        buttonClcikSound.play(1.0f);
                    }
                    return;
                }
                if (gameGrid.gameOver() || gameGrid.gameLevelComplete()){
                    game.setScreen(new GameOverScreen(game));
                    return;
                }
                if (inBounds(event, gameGridXPos, gameGridYPos, gameGrid.getWidth(), gameGrid.getHeight()) && touchTimes == 0){
                    firstTouchX = event.x;
                    firstTouchY = event.y;
                    gameGrid.highLightSelected(firstTouchX, firstTouchY);
                    touchTimes++;
                    return;
                }
                else if(inBounds(event, gameGridXPos, gameGridYPos, gameGrid.getWidth(), gameGrid.getHeight()) && touchTimes == 1)
                {
                    secondTouchX = event.x;
                    secondTouchY = event.y;
                    gameGrid.highLightSelected(secondTouchX, secondTouchY);
                    gameGrid.update(firstTouchX,firstTouchY,secondTouchX,secondTouchY);
                    touchTimes = 0;
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime){
        Graphics g = game.getGraphics();
        g.drawPixmap(background, 20, -10);
        g.drawPixmap(optionsButton, optionXPos, optionYPos);
        gameGrid.show(g);
        if(gameGrid.gameOver())
        {
            g.drawPixmap(gameOverImage, gameOverXPos, gameOverYPos);
            if(playSoundCount == 0 && mMyAppsBundle.getInt("soundEffectOn") == 1)
            {
                loseLevelSound.play(1.0f);
                playSoundCount++;
            }
        }

        if(gameGrid.gameLevelComplete())
        {
            isGameLevelComplete = true;
            g.drawPixmap(gameLevelCompleteImage, gameOverXPos, gameOverYPos);
            if(playSoundCount == 0 && mMyAppsBundle.getInt("soundEffectOn") == 1)
            {
                winLevelSound.play(1.0f);
                playSoundCount++;
            }
        }

        text.drawLetters("Level: ", 310, textY-50, textColor, textSize_Big, textFileName);
        text.drawLetters(Integer.toString(mMyAppsBundle.getInt("level")), 460, textY-50, textColor, textSize_Big, textFileName);
        text.drawLetters("Target: ", 10, textY, textColor, textSize, textFileName);
        text.drawLetters(Integer.toString(gameGrid.getTargetScore()), 130, textY, textColor, textSize, textFileName);
        text.drawLetters("Moves: ", 310, textY, textColor, textSize, textFileName);
        text.drawLetters(Integer.toString(gameGrid.getMoves()), 430, textY, textColor, textSize, textFileName);
        text.drawLetters("Score: ", 510, textY, textColor, textSize, textFileName);
        text.drawLetters(Integer.toString(gameGrid.getScore()), 630, textY, textColor, textSize, textFileName);
    }

    @Override
    public void pause(){    }

    @Override
    public void resume(){    }

    @Override
    public void dispose()
    {
        backgroundMusic.stop();
        if(isGameLevelComplete)
        {
            int level;
            level = mMyAppsBundle.getInt("level");
            level++;
            mMyAppsBundle.putInt("level",level);
        }
    }
}
