package com.filip.androidgames.framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface Game
{
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public Text getText();  //Lab4
    public Audio getAudio();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getStartScreen();

    public boolean isSignedIn();
    public void signIn();
    public void submitScore(int score);
    public void showLeaderboard();
    public void showAchievements();

    public void unlockAchievementOne();

    public void showBanner();
    public void hideBanner();

    public void showInterstitialAd();
}

