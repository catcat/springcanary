package com.orcl.logic;

import org.springframework.beans.factory.annotation.Required;

public class Apple {
    protected Pear redPear;

    protected Pear pear;
    protected int color;

    public Apple(Pear pear) {
        this.pear = pear;
    }

    public Pear getPear() {
        return pear;
    }


    public void setPear(Pear pear) {
        this.pear = pear;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Pear getRedPear() {
        return redPear;
    }


    @Required
    public void setRedPear(Pear redPear) {
        this.redPear = redPear;
    }
}
