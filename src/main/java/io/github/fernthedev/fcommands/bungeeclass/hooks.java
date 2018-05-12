package io.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.api.ProxyServer;

public class hooks {

    private static boolean isAdvancedBanEnabled = false;

    private static hooks thisInstance;

    public hooks() {
        thisInstance = this;
        loadHooks();
    }

    public static hooks getInstance() {
        return thisInstance;
    }

    public void loadHooks() {
        if(ProxyServer.getInstance().getPluginManager().getPlugin("AdvancedBan") !=null) {
            isAdvancedBanEnabled = true;
        }
    }

    public boolean isHookEnabled(String plugin) {
        if(plugin == null) {
            return false;
        }else
        return ProxyServer.getInstance().getPluginManager().getPlugin(plugin) != null;
    }

    public boolean hasIsAdvancedBan() {
        return isAdvancedBanEnabled;
    }

}
