package com.orcl.logic;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Garden implements ApplicationListener {
    @Bean(initMethod = "onInit", name = "codePear")
    public Pear getNewPear() {
        return new Pear();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
