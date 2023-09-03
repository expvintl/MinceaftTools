package com.expvintl.mctools.events.client;

public class PreTickEvent {
    private static final PreTickEvent INSTANCE=new PreTickEvent();
    public static PreTickEvent get(){
        return INSTANCE;
    }
}
