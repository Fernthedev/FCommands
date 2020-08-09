package com.github.fernthedev.fcommands.proxy.data;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;

@Data
public class SeenPlayerValue {

    //f99a57670aae48caa8a86b56e6a8c470:
    //  time: Sep-2019 24 13.33
    //  server: survival

    @NonNull
    private Date time;

    @NonNull
    private String server;

}
