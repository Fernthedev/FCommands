package io.github.fernplayzz.fcommands.spigotclass.ncp;

import fr.neatmonster.nocheatplus.checks.CheckType;
import io.github.fernplayzz.fcommands.spigotclass.spigot;

public class playerreport {
    private final String player;
    private final String server;
    private final CheckType check;
    private final double violation;

    public playerreport(String player, CheckType check, double violation) {
        this.player = player;
        this.server = spigot.SERVER_NAME;
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
