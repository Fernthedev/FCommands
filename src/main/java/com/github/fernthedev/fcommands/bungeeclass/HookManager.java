package com.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.api.ProxyServer;

public class HookManager {

    private static boolean isAdvancedBanEnabled = false;

    private static HookManager thisInstance;

    public HookManager() {
        thisInstance = this;
        loadHooks();
    }

    public static HookManager getInstance() {
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

    public boolean hasAdvancedBan() {
        return isAdvancedBanEnabled;
    }


    public static void onDisable() {
        isAdvancedBanEnabled = false;
        thisInstance = null;
    }
}
