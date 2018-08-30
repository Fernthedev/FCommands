package io.github.fernthedev.fcommands.bungeeclass;

public abstract class MessageRunnable implements Runnable {

    private boolean ran;

    public void run() {
        FernCommands.getInstance().getLogger().info("Run the messagerunnable");
        if(!ran) {
            ran = true;
        }
    }


}
