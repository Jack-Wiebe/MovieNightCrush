package com.filip.androidgames.framework.impl;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.filip.androidgames.framework.Audio;
import com.filip.androidgames.framework.FileIO;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Input;
import com.filip.androidgames.framework.Screen;
import com.filip.androidgames.framework.Text;
import com.filip.movienightcrush.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public abstract class AndroidGame extends BaseGameActivity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Text text;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;

    static final int REQUEST_LEADERBOARD = 100;
    static final int REQUEST_ACHIEVEMENTS = 200;

    AdView adView;

    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? 1280 : 800;
        int frameBufferHeight = isLandscape ? 800 : 1280;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
        
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        
        // determine the scale based on our framebuffer and our display sizes
        float scaleX = (float) frameBufferWidth / size.x;
        float scaleY = (float) frameBufferHeight / size.y;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        text = new AndroidText(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);

        //1. Adding Banner Adds
        //Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");

        //Create and load the AdView.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        adView.setAdSize(AdSize.SMART_BANNER);

        //Create a RelativeLayout as the main layout and add the gameView.
        RelativeLayout mainLayout = new RelativeLayout(this);
        mainLayout.addView(renderView);

        //Add adView to the top of the screen.
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        adParams.addRule(RelativeLayout.ALIGN_TOP);
        adParams.addRule(RelativeLayout.ALIGN_BOTTOM);
        mainLayout.addView(adView, adParams);

        //Set the RelativeLayout as the main layout.
        setContentView(mainLayout);

        //2. Adding Interstitial Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        screen = getStartScreen();
//        setContentView(renderView);
    }

    @Override
    public void onResume() {
        super.onResume();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Text getText() { return text;}

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen is null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen() {
        return screen;
    }

    public boolean isSignedIn()
    {
        return getGameHelper().isSignedIn();
    }
    public void signIn()
    {
        getGameHelper().beginUserInitiatedSignIn();
    }
    public void submitScore(int score)
    {
        Games.Leaderboards.submitScore(getGameHelper().getApiClient(),
                getString(R.string.leaderboard_movie_night_crush),
                score);
    }
    public void showLeaderboard()
    {
        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(),
                getString(R.string.leaderboard_movie_night_crush)), REQUEST_LEADERBOARD);
    }
    public void showAchievements()
    {
        startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
    }
    public void unlockAchievementOne()
    {
        Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_popdrink));
        Games.Achievements.increment(getApiClient(), getString(R.string.achievement_popdrink), 1);
    }

    public void showBanner()
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                adView.setVisibility(View.VISIBLE);
//                adView.loadAd(new AdRequest.Builder().build());
                adView.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                //replace "AdRequest.DEVICE_ID_EMULATOR" to "2502D554C3E469427B643A3BB7F2E807"
//                .addTestDevice("2502D554C3E469427B643A3BB7F2E807").build());
            }
        });
    }

    public void hideBanner()
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                adView.setVisibility(View.GONE);
            }
        });
    }

    public void showInterstitialAd()
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
            }
        });
    }
}