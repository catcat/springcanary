package com.orcl.logic;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Pear implements ApplicationContextAware {
    protected int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    
    public static Pear getInstance() {
        return new Pear();
    }

    public static void onInit() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
