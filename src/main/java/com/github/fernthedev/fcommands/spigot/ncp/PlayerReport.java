package com.github.fernthedev.fcommands.spigot.ncp;

import fr.neatmonster.nocheatplus.checks.CheckType;

public class PlayerReport {
    private final String player;
    private final String server;
    private final CheckType check;
    private final double violation;

    public PlayerReport(String player, CheckType check, double violation, String serverName) {
        this.player = player;
        this.server = serverName;
        this.check = check;
        this.violation = violation;
    }

    /**
     * @return String The name of the reported player.
     */
    public String getPlayer() {
        return this.player;
    }

    /**
     * @return String The server name of the reported player.
     */
    public String getServer() {
        return this.server;
    }

    /**
     * @return CheckType The check/cheat that was detected.
     */
    public CheckType getCheckType() {
        return this.check;
    }

    /**
     * @return Double The violation of the check/cheat
     */
    public double getViolation() {
        return this.violation;
    }
}
