package com.github.fernthedev.fcommands.spigotclass.ncp;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import fr.neatmonster.nocheatplus.checks.CheckType;

public class playerreport {
    private final String player;
    private final String server;
    private final CheckType check;
    private final double violation;

    public playerreport(String player, CheckType check, double violation) {
        this.player = player;
        this.server = FernCommands.SERVER_NAME;
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