package com.filip.movienightcrush;


import com.filip.androidgames.framework.Graphics;
import com.filip.androidgames.framework.Pixmap;

public class FoodPiece {
    private Graphics m_Graphics;
    private int m_FoodTypeValue;
    private int m_PositionX;
    private int m_PositionY;

    private Pixmap m_FoodImage;

    public FoodPiece(Graphics graphics, int foodTypeValue, int positionX, int positionY){
        m_Graphics = graphics;
        m_FoodTypeValue = foodTypeValue;
        m_PositionX = positionX;
        m_PositionY = positionY;

        updateFoodImage(foodTypeValue);
    }

    private void updateFoodImage(int foodTypeValue)
    {
        //change m_FoodImage according to foodTypeValue
        switch (foodTypeValue)
        {
            case 0://POPCORN:
                //m_FoodImage = m_Graphics.newPixmap("popcorn.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_1.png", Graphics.PixmapFormat.RGB565);
                break;
            case 1://POPDRINK:
                //m_FoodImage = m_Graphics.newPixmap("popdrink.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_2.png", Graphics.PixmapFormat.RGB565);
                break;
            case 2://NACHO:
                //m_FoodImage = m_Graphics.newPixmap("nacho.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_3.png", Graphics.PixmapFormat.RGB565);
                break;
            case 3://CHOCOLATEBAR:
                //m_FoodImage = m_Graphics.newPixmap("chocolatebar.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_4.png", Graphics.PixmapFormat.RGB565);
                break;
            case 4://BEER:
                //m_FoodImage = m_Graphics.newPixmap("beer.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_5.png", Graphics.PixmapFormat.RGB565);
                break;
            case 5://TICKET:
                //m_FoodImage = m_Graphics.newPixmap("ticket.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_6.png", Graphics.PixmapFormat.RGB565);
                break;
            case 6://HOTDOG:
                //m_FoodImage = m_Graphics.newPixmap("hotdog.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_7.png", Graphics.PixmapFormat.RGB565);
                break;
            case 7://FRIES:
                //m_FoodImage = m_Graphics.newPixmap("fries.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_8.png", Graphics.PixmapFormat.RGB565);
                break;
            case 8://EMPTY:
                //m_FoodImage = m_Graphics.newPixmap("empty.png", Graphics.PixmapFormat.RGB565);
                m_FoodImage = m_Graphics.newPixmap("m_9.png", Graphics.PixmapFormat.RGB565);
                break;
        }
    }

    public Graphics getM_Graphics() {
        return m_Graphics;
    }

    public void setM_Graphics(Graphics m_Graphics) {
        this.m_Graphics = m_Graphics;
    }

    public int getM_FoodTypeValue() {
        return m_FoodTypeValue;
    }

    public void setM_FoodTypeValue(int m_FoodTypeValue) {
        this.m_FoodTypeValue = m_FoodTypeValue;
        updateFoodImage(m_FoodTypeValue);
    }

    public Pixmap getM_FoodImage() {
        return m_FoodImage;
    }

    public int getM_PositionX() {
        return m_PositionX;
    }

    public void setM_PositionX(int m_PositionX) {
        this.m_PositionX = m_PositionX;
    }

    public int getM_PositionY() {
        return m_PositionY;
    }

    public void setM_PositionY(int m_PositionY) {
        this.m_PositionY = m_PositionY;
    }
}
