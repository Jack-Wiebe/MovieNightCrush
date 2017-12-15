package com.filip.movienightcrush;



public enum FoodType {
    POPCORN(0),         //0
    NACHO(1),           //1
    CHOCOLATEBAR(2),    //2
    POPDRINK(3),        //3
    BEER(4),            //4
    TICKET(5),          //5
    HOTDOG(6),          //6
    FRIES(7),           //7
    EMPTY(8);           //8

    private int m_Value;

    FoodType(int value)
    {
        m_Value = value;
    }

    public int getValue()
    {
        return m_Value;
    }

    public void setValue(int value)
    {
        m_Value = value;
    }
}
