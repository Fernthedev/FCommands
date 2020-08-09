package com.github.fernthedev.fcommands.universal;

import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.Getter;
import lombok.Setter;

public class UniversalMysql {

    @Setter
    @Getter
    private static DatabaseListener databaseManager;

}
