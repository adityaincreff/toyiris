package com.increff.toyiris.module;

import org.springframework.beans.factory.annotation.Autowired;

public class NoosModule extends AbstractModule{
    @Autowired
    private ModuleData moduleData;
    @Override
    public void run(){
        
    }
}
