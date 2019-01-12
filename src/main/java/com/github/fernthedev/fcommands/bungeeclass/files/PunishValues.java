package com.github.fernthedev.fcommands.bungeeclass.files;

public class PunishValues {

    private String PermBan = "&c&lYOU HAVE BEEN PERMANENTLY BANNED";
    private String TempBan = "&c&lYOU HAVE BEEN BANNED UNTIL %time%";


    public String getPermBan() {
        return PermBan;
    }

    public void setPermBan(String permBan) {
        PermBan = permBan;
    }

    public String getTempBan() {
        return TempBan;
    }

    public void setTempBan(String tempBan) {
        TempBan = tempBan;
    }
}
