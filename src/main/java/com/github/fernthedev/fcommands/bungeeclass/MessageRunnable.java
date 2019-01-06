package com.github.fernthedev.fcommands.bungeeclass;


public abstract class MessageRunnable implements Runnable {

    private boolean ran;



    public MessageRunnable() {

    }


    public void run() {
        if(!ran) {
            ran = true;
        }
    }


}
