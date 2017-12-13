package com.filip.movienightcrush;

import com.filip.androidgames.framework.Audio;
import com.filip.androidgames.framework.Game;
import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Sound;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import static com.filip.movienightcrush.MovieNightCrush.mMyAppsBundle;

public class Grid
{
    Random random = new Random();
    private static final int FOODTYPECOUNT = 9;
    private static final int SCREENWIDTH = 800;
    private static final int SCREENHEIGHT = 1280;
    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final int FOODPIECEWIDTH = Math.round(SCREENWIDTH / ROWS);   //FOODPIECEWIDTH = 88;
    private static final int FOODPIECEHEIGHT = FOODPIECEWIDTH;

    public static final int SCREENWIDTHOFFSET = SCREENWIDTH / 2 - (FOODPIECEWIDTH * ROWS) / 2;
    public static final int SCREENHEIGHTOFFSET = 150;

    private boolean renewGrid = false;
    private FoodPiece[][] foodPieces;

    private  Game m_Game;
    private Graphics m_Graphics;

    private Sound swapSound;
    private Sound invalidSwapSound;
    private Sound matchSound;
    private Sound fallingFoodSound;
    private Sound addFoodSound;

    private int targetScore = 180;                // = 12345 or 240
    private static final int STARTMOVES = 5;        // = 30
    private int moves = STARTMOVES;
    private static final int BASICSCORE = 60;       // = 60
    private int score = 0;
    private int lastScore = 0;

    private boolean isGameOver = false;
    private boolean isGameLevelComplete = false;
    private boolean isUpdatedLastScore = false;

    public Grid(Game game, Graphics graphics)
    {
        m_Game = game;
        m_Graphics = graphics;

        foodPieces = new FoodPiece[COLS][ROWS];

        for(int r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
            {
                int randomFoodTypeValue = random.nextInt(FOODTYPECOUNT - 3);    //include the first six images only
                foodPieces[r][c] = new FoodPiece(m_Graphics,
                                                randomFoodTypeValue,
                                                c*FOODPIECEWIDTH + SCREENWIDTHOFFSET,
                                                r*FOODPIECEHEIGHT + SCREENHEIGHTOFFSET);
            }
        }

        Audio a = game.getAudio();
        swapSound = a.newSound("Sounds/swapSound.wav");
        invalidSwapSound = a.newSound("Sounds/invalidSwapSound.wav");
        matchSound = a.newSound("Sounds/matchSound.wav");
        fallingFoodSound = a.newSound("Sounds/fallingFoodSound.wav");
        addFoodSound = a.newSound("Sounds/addFoodSound.wav");
    }

    public void update(int firstTouchX,int firstTouchY, int secondTouchX, int secondTouchY)
    {
        if((Math.abs(firstTouchX - secondTouchX) <= FOODPIECEWIDTH + SCREENWIDTHOFFSET) && (Math.abs(firstTouchY - secondTouchY) <= FOODPIECEHEIGHT + SCREENHEIGHTOFFSET))
        {
            int r1=0,c1=0,r2=0,c2=0;
            for (int r = 0; r < ROWS; r++)
            {
                for (int c = 0; c < COLS; c++)
                {
                    if(firstTouchX >= c*FOODPIECEWIDTH + SCREENWIDTHOFFSET && firstTouchX < (c+1)*FOODPIECEWIDTH + SCREENWIDTHOFFSET)
                    {
                        c1=c;
                    }
                    if(firstTouchY >= r*FOODPIECEHEIGHT + SCREENHEIGHTOFFSET && firstTouchY < (r+1) *FOODPIECEHEIGHT + SCREENHEIGHTOFFSET)
                    {
                        r1=r;
                    }
                    if(secondTouchX >= c*FOODPIECEWIDTH + SCREENWIDTHOFFSET && secondTouchX < (c+1)*FOODPIECEWIDTH + SCREENWIDTHOFFSET)
                    {
                        c2=c;
                    }
                    if(secondTouchY >= r*FOODPIECEHEIGHT + SCREENHEIGHTOFFSET && secondTouchY < (r+1) *FOODPIECEHEIGHT + SCREENHEIGHTOFFSET)
                    {
                        r2=r;
                    }
                }
            }
            if(getSwapable(r1,c1,r2,c2))
            {
                swap(foodPieces[r1][c1], foodPieces[r2][c2]);
                if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                {
                    swapSound.play(1.0f);
                }
                moves--;
                if(moves == 0)
                {
                    isGameOver = true;
                }
            }
            else
            {
                if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                {
                    invalidSwapSound.play(1.0f);
                }
            }
        }
    }

    public void swap(FoodPiece foodPiece1, FoodPiece foodPiece2)
    {
        int tempFoodTypeValue;

        tempFoodTypeValue = foodPiece1.getM_FoodTypeValue();
        foodPiece1.setM_FoodTypeValue((foodPiece2.getM_FoodTypeValue()));
        foodPiece2.setM_FoodTypeValue(tempFoodTypeValue);
    }

    public boolean getSwapable(int r1,int c1,int r2,int c2)
    {
        boolean swapable = false;
        int r1FoodTypeValue = foodPieces[r1][c1].getM_FoodTypeValue();
        int r2FoodTypeValue = foodPieces[r2][c2].getM_FoodTypeValue();

        if(checkFourDirectionsSwapable(r1, c1, r2, c2, r2FoodTypeValue, r1FoodTypeValue)
                || checkFourDirectionsSwapable(r2, c2, r1, c1, r1FoodTypeValue, r2FoodTypeValue))
        {
            swapable = true;
            return swapable;
        }
        return swapable;
    }

    public boolean checkFourDirectionsSwapable(int r1,int c1, int r2,int c2, int r2FoodTypeValue, int r1FoodTypeValue)
    {
        boolean swapable = false;
        if((r1 > 1 && r1 < ROWS-2) && (c1 > 1 && c1 < COLS-2))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                swapable = true;
            }

        }
        if((r1 == 0 && c1 == 0) || (r1 == 1 && c1 == 0))
        {
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if(r1 == 1)
            {
                int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 > r2) {
                    top_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }

        if((r1 == 0 && c1 == 1) || (r1 == 1 && c1 == 1))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if(r1 == 1)
            {
                int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 > r2) {
                    top_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 > 1 && r1 < ROWS-2 && c1 == 0) || (r1 > 1 && r1 < ROWS-2 && c1 == 1))
        {
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();

            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                swapable = true;
            }
            if(c1 == 1)
            {
                int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
                //move left
                if(r1 == r2 && c1 > c2) {
                    left_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == ROWS-1 && c1 == 0) || (r1 == ROWS-2 && c1 == 0))
        {
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if(r1 == ROWS-2)
            {
                int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 < r2) {
                    bottom_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == ROWS-1 && c1 == 1) || (r1 == ROWS-2 && c1 == 1))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if(r1 == ROWS-2)
            {
                int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 < r2) {
                    bottom_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == 0 && c1 == COLS-1) || (r1 == 1 && c1 == COLS-1))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if(r1 == 1)
            {
                int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 > r2) {
                    top_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == 0 && c1 == COLS-2) || (r1 == 1 && c1 == COLS-2))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if(r1 == 1)
            {
                int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 > r2) {
                    top_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 > 1 && r1 < ROWS-2 && c1 == COLS-1) || (r1 > 1 && r1 < ROWS-2 && c1 == COLS-2))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                swapable = true;
            }
            if(c1 == COLS-2)
            {
                int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
                if(r1 == r2 && c1 < c2) {
                    right_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == ROWS-1 && c1 == COLS-1) || (r1 == ROWS-2 && c1 == COLS-1))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if(r1 == ROWS-2)
            {
                int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 < r2) {
                    bottom_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == ROWS-1 && c1 == COLS-2) || (r1 == ROWS-2 && c1 == COLS-2))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if(r1 == ROWS-2)
            {
                int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 < r2) {
                    bottom_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == 0 && c1 > 1 && c1 < ROWS-2) || (r1 == 1 && c1 > 1 && c1 < ROWS-2))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
            int bottom_2 = foodPieces[r1+2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 < r2) {
                bottom_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == bottom_1 && r2FoodTypeValue == bottom_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if(r1 == 1)
            {
                int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 > r2) {
                    top_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        if((r1 == COLS-1 && c1 > 1 && c1 < ROWS-2) || (r1 == COLS-2 && c1 > 1 && c1 < ROWS-2))
        {
            int left_1 = foodPieces[r1][c1-1].getM_FoodTypeValue();
            int left_2 = foodPieces[r1][c1-2].getM_FoodTypeValue();
            int right_1 = foodPieces[r1][c1+1].getM_FoodTypeValue();
            int right_2 = foodPieces[r1][c1+2].getM_FoodTypeValue();
            int top_1 = foodPieces[r1-1][c1].getM_FoodTypeValue();
            int top_2 =  foodPieces[r1-2][c1].getM_FoodTypeValue();

            if(r1 == r2 && c1 > c2) {
                left_1 = r1FoodTypeValue;
            }
            if(r1 == r2 && c1 < c2) {
                right_1 = r1FoodTypeValue;
            }
            if(c1 == c2 && r1 > r2) {
                top_1 = r1FoodTypeValue;
            }

            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == left_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == right_1 && r2FoodTypeValue == right_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == top_1 && r2FoodTypeValue == top_2) {
                swapable = true;
            }
            if (r2FoodTypeValue == left_1 && r2FoodTypeValue == right_1) {
                swapable = true;
            }
            if(r1 == COLS-2)
            {
                int bottom_1 = foodPieces[r1+1][c1].getM_FoodTypeValue();
                if(c1 == c2 && r1 < r2) {
                    bottom_1 = r1FoodTypeValue;
                }
                if (r2FoodTypeValue == top_1 && r2FoodTypeValue == bottom_1) {
                    swapable = true;
                }
            }
        }
        return swapable;
    }

    public int getWidth(){return FOODPIECEWIDTH * ROWS;}

    public int getHeight(){return FOODPIECEHEIGHT * COLS;}

    public void show(Graphics graphics)
    {
        for(int r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
            {
                graphics.drawPixmap(foodPieces[r][c].getM_FoodImage(),
                                    foodPieces[r][c].getM_PositionX(),
                                    foodPieces[r][c].getM_PositionY());
            }
        }
    }

    public void checkMatches()
    {
        boolean isAddingScore = false;
        int counter;
        for(int r = 0; r < ROWS; r++)
        {
            counter = 1;
            for(int c = 1; c < COLS; c++)
            {
                if(foodPieces[r][c].getM_FoodTypeValue() != 8 && foodPieces[r][c-1].getM_FoodTypeValue() != 8) {
                    if (foodPieces[r][c].getM_FoodTypeValue() == foodPieces[r][c-1].getM_FoodTypeValue()) {
                        counter++;
                    } else {
                        counter = 1;
                    }
                    if (counter == 3) {
                        foodPieces[r][c].setM_FoodTypeValue(8);
                        foodPieces[r][c-1].setM_FoodTypeValue(8);
                        foodPieces[r][c-2].setM_FoodTypeValue(8);
                        renewGrid = true;
                        counter = 1;
                        if(moves < STARTMOVES)  //after Moving
                        {
                            score += BASICSCORE;
                            isAddingScore = true;
                            if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                            {
                                matchSound.play(1.0f);
                            }
                        }
                    }
                }
            }
        }
        for(int c = 0; c < COLS; c++)
        {
            counter = 1;
            for(int r = 1; r < ROWS; r++)
            {
                if(foodPieces[r][c].getM_FoodTypeValue() != 8 && foodPieces[r-1][c].getM_FoodTypeValue() != 8) {
                    if (foodPieces[r][c].getM_FoodTypeValue() == foodPieces[r-1][c].getM_FoodTypeValue()) {
                        counter++;
                    } else {
                        counter = 1;
                    }
                    if (counter == 3) {
                        foodPieces[r][c].setM_FoodTypeValue(8);
                        foodPieces[r-1][c].setM_FoodTypeValue(8);
                        foodPieces[r-2][c].setM_FoodTypeValue(8);
                        renewGrid = true;
                        counter = 1;
                        if(moves < STARTMOVES)
                        {
                            score += BASICSCORE;
                            isAddingScore = true;
                            if(mMyAppsBundle.getInt("soundEffectOn") == 1)
                            {
                                matchSound.play(1.0f);
                            }
                        }
                    }
                }
            }
        }

        if(renewGrid)
        {
            refreshGrid();
            renewGrid = false;
        }
        if(score > targetScore && !isAddingScore)
        {
            isGameLevelComplete = true;
        }
    }

    public void refreshGrid()
    {
        boolean anyMoved = false;

        for(int r = ROWS-1; r > 0; r--)
        {
            for (int c = COLS-1; c >= 0; c--)
            {
                for (int col = COLS-1; col >= 0; col--)
                {
                    if(foodPieces[0][col].getM_FoodTypeValue() == 8)
                    {
                        int randomFoodTypeValue = random.nextInt(FOODTYPECOUNT - 3);
                        foodPieces[0][col].setM_FoodTypeValue(randomFoodTypeValue);
                        if(moves < STARTMOVES && mMyAppsBundle.getInt("soundEffectOn") == 1)
                        {
                            //addFoodSound.play(1.0f);
                        }
                    }
                }
                if(foodPieces[r][c].getM_FoodTypeValue() == 8)
                {
                    if(foodPieces[r-1][c].getM_FoodTypeValue() != 8)
                    {
                        foodPieces[r][c].setM_FoodTypeValue(foodPieces[r-1][c].getM_FoodTypeValue());
                        foodPieces[r-1][c].setM_FoodTypeValue(8);
                        if(moves < STARTMOVES && mMyAppsBundle.getInt("soundEffectOn") == 1)  //after Moving
                        {
                            fallingFoodSound.play(1.0f);
                        }
                        anyMoved = true;
                    }
                }
            }
        }

        if(anyMoved)
        {
            refreshGrid();
        }
    }

    public boolean gameLevelComplete()
    {
        if(isGameLevelComplete && !isUpdatedLastScore)
        {
            updateLastScore();
            isUpdatedLastScore = true;
        }
        return  isGameLevelComplete;
    }

    public boolean gameOver()
    {
        if(isGameOver && !isUpdatedLastScore)
        {
            updateLastScore();
            isUpdatedLastScore = true;
        }
        return isGameOver;
    }

    public void updateLastScore()
    {
        lastScore = score;
        m_Game.submitScore(lastScore);
    }

    public int getTargetScore() {
        return targetScore;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}