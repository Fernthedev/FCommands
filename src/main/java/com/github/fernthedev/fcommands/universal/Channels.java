package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fernapi.universal.data.network.Channel;

public class Channels {

    private Channels() {
    }

    public static final Channel NICK_CHANNEL = new Channel("ferncommands", "nick", Channel.ChannelAction.BOTH);
    public static final String NICK_RELOADNICKSQL = "ReloadNickSQL";

}
