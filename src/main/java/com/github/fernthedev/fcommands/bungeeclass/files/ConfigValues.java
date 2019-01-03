package com.github.fernthedev.fcommands.bungeeclass.files;

public class ConfigValues {

    private String motd = "&bThis is the default ferncommand motd \\n which supports linebreaks.";
    private boolean useMotd = false;

    private int[] portChecks = new int[] {25565};

    private boolean cacheIps = false;
    private boolean allowIPDelete = false;
    private boolean allowIPShow = false;
    private boolean showAltsCommand = false;

    private boolean allowNameHistory = true;

    private boolean globalNicks = false;
    private boolean showPing = false;
    private boolean allowSeenCommand = false;
    private boolean punishMotd = false;
    private String offlineServerMotd = "&cSERVER UNDER MAINTENANCE!";

    //DataBase vars.
    private String username = "root"; //Enter in your db username
    private String password = "pass"; //Enter your password for the db
    private String port = "3306";
    private String urlHost = "localhost";
    private String database = "database";

    public boolean isCacheIps() {
        return cacheIps;
    }

    public void setCacheIps(boolean cacheIps) {
        this.cacheIps = cacheIps;
    }

    public boolean isAllowIPDelete() {
        return allowIPDelete;
    }

    public void setAllowIPDelete(boolean allowIPDelete) {
        this.allowIPDelete = allowIPDelete;
    }

    public boolean isShowAltsCommand() {
        return showAltsCommand;
    }

    public void setShowAltsCommand(boolean showAltsCommand) {
        this.showAltsCommand = showAltsCommand;
    }

    public boolean isGlobalNicks() {
        return globalNicks;
    }

    public void setGlobalNicks(boolean globalNicks) {
        this.globalNicks = globalNicks;
    }

    public boolean isShowPing() {
        return showPing;
    }

    public void setShowPing(boolean showPing) {
        this.showPing = showPing;
    }

    public boolean isAllowSeenCommand() {
        return allowSeenCommand;
    }

    public void setAllowSeenCommand(boolean allowSeenCommand) {
        this.allowSeenCommand = allowSeenCommand;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public boolean isUseMotd() {
        return useMotd;
    }

    public void setUseMotd(boolean useMotd) {
        this.useMotd = useMotd;
    }

    public int[] getPortChecks() {
        return portChecks;
    }

    public void setPortChecks(int[] portChecks) {
        this.portChecks = portChecks;
    }

    public boolean isPunishMotd() {
        return punishMotd;
    }

    public void setPunishMotd(boolean punishMotd) {
        this.punishMotd = punishMotd;
    }

    public String getOfflineServerMotd() {
        return offlineServerMotd;
    }

    public void setOfflineServerMotd(String offlineServerMotd) {
        this.offlineServerMotd = offlineServerMotd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrlHost() {
        return urlHost;
    }

    public void setUrlHost(String urlHost) {
        this.urlHost = urlHost;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public boolean isAllowIPShow() {
        return allowIPShow;
    }

    public void setAllowIPShow(boolean allowIPShow) {
        this.allowIPShow = allowIPShow;
    }

    public boolean isAllowNameHistory() {
        return allowNameHistory;
    }

    public void setAllowNameHistory(boolean allowNameHistory) {
        this.allowNameHistory = allowNameHistory;
    }
}
