package com.filip.movienightcrush;

import android.os.Bundle;

import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.impl.AndroidGame;


public class MovieNightCrush extends AndroidGame {
    public static Bundle mMyAppsBundle = new Bundle();

    @Override
    public Screen getStartScreen() {
        mMyAppsBundle.putInt("musicOn",1);
        mMyAppsBundle.putInt("soundEffectOn",1);

        return new MainMenuScreen(this);
    }
}
